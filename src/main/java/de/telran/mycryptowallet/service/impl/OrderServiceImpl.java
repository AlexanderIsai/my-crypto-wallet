package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.repository.OrderRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 25.01.2024.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ActiveUserService activeUserService;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final UserService userService;
    private final OperationService operationService;

    @Override
    @Transactional
    public void addOrder(OrderAddDTO orderDTO) {
        Order order = new Order();

        User orderUser = activeUserService.getActiveUser();
        order.setUser(orderUser);

        Currency orderCurrency = currencyService.getCurrencyByCode(orderDTO.getCurrencyCode());
        order.setCurrency(orderCurrency);

        order.setRateValue(orderDTO.getOrderRate());

        order.setAmount(orderDTO.getAmount());

        order.setType(orderDTO.getOperationType());
        order.setStatus(OrderStatus.ACTIVE);
        orderRepository.save(order);
        accountService.reserveForOrder(orderDTO);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    @Override
    public void updateOrder(Long id, Order order) {
        order.setId(id);
        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findOrderById(id);
    }

    @Override
    @Transactional
    public void executeOrder(Long orderId) {
        Order order = getOrderById(orderId); // нужный ордер
        User executorOrder = activeUserService.getActiveUser(); //юзер, который хочет его выполнить
        User ownerOrder = userService.getUserById(order.getUser().getId());//юзер, который владеет ордером
        Account ownerBasicAccount = accountService.getAccountByUserIdAndCurrency(ownerOrder.getId(), currencyService.getBasicCurrency()).orElseThrow(); //счет владельца ордера в базовой валюте (доллар), потому что он хочет купить крипту
        Account ownerOrderAccount = accountService.getAccountByUserIdAndCurrency(ownerOrder.getId(), order.getCurrency().getCode()).orElseThrow(); // счет владельца ордера в валюте ордера - туда должны упасть Битки

        Account executerBasicAccount = accountService.getAccountByUserIdAndCurrency(executorOrder.getId(), currencyService.getBasicCurrency()).orElseThrow(); //счет исполнителя ордера в базовой валюте (доллар), потому что он хочет продать крипту
        Account executerOrderAccount = accountService.getAccountByUserIdAndCurrency(executorOrder.getId(), order.getCurrency().getCode()).orElseThrow(); //счет исполнителя ордера в валюте ордера - сейчас Биток
        OperationType operationType = order.getType();

        cancelOrder(order.getId());
        if (operationType.equals(OperationType.BUY)){
            operationService.transfer(ownerBasicAccount.getId(), executerBasicAccount.getId(), order.getAmount().multiply(order.getRateValue()));// с долларового счета владельца ордера переводим бабло на долларовый счет исполнителя ордера
            operationService.transfer(executerOrderAccount.getId(), ownerOrderAccount.getId(), order.getAmount());
            operationService.addOrderOperation(ownerOrder, executorOrder, order);
        }
        else {
            operationService.transfer(executerBasicAccount.getId(), ownerBasicAccount.getId(), order.getAmount().multiply(order.getRateValue()));// с долларового счета владельца ордера переводим бабло на долларовый счет исполнителя ордера
            operationService.transfer(ownerOrderAccount.getId(), executerOrderAccount.getId(), order.getAmount());
            operationService.addOrderOperation(executorOrder, ownerOrder, order);
        }
        order.setStatus(OrderStatus.DONE);
        orderRepository.save(order);
    }
    //TODO подумать про добавление операций
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOrderById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        Account orderAccount = accountService.getAccountFromOrder(order);//аккаунт, где заморожены деньги
        BigDecimal orderAmount = orderAccount.getOrderBalance();
        orderAccount.setOrderBalance(BigDecimal.ZERO);
        orderAccount.setBalance(orderAccount.getBalance().add(orderAmount));
        accountService.updateAccount(orderAccount.getId(), orderAccount);
        updateOrder(orderId, order);
    }
}
