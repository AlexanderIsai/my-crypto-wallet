package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.exceptions.NotActiveOrder;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;
import de.telran.mycryptowallet.repository.OrderRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public void addOrder(OrderAddDTO orderDTO) throws NotEnoughFundsException {
        Order order = new Order();

        User orderUser = activeUserService.getActiveUser();
        order.setUser(orderUser);

        Currency orderCurrency = currencyService.getCurrencyByCode(orderDTO.getCurrencyCode());
        order.setCurrency(orderCurrency);

        order.setRateValue(orderDTO.getOrderRate());

        order.setAmount(orderDTO.getAmount());

        order.setType(orderDTO.getOperationType());
        order.setStatus(OrderStatus.ACTIVE);

        accountService.reserveForOrder(orderDTO);
        orderRepository.save(order);
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
    public void executeOrder(Long orderId) throws NotActiveOrder {
        Order order = getOrderById(orderId);// нужный ордер
        if (!order.getStatus().equals(OrderStatus.ACTIVE)){
            throw new NotActiveOrder("This order is not active");
        }
        User executorOrder = activeUserService.getActiveUser(); //юзер, который хочет его выполнить
        User ownerOrder = userService.getUserById(order.getUser().getId());//юзер, который владеет ордером
        Account ownerBasicAccount = accountService.getAccountByUserIdAndCurrency(ownerOrder.getId(), currencyService.getBasicCurrency()).orElseThrow(); //счет владельца ордера в базовой валюте (доллар), потому что он хочет купить крипту
        Account ownerOrderAccount = accountService.getAccountByUserIdAndCurrency(ownerOrder.getId(), order.getCurrency().getCode()).orElseThrow(); // счет владельца ордера в валюте ордера - туда должны упасть Битки

        Account executerBasicAccount = accountService.getAccountByUserIdAndCurrency(executorOrder.getId(), currencyService.getBasicCurrency()).orElseThrow(); //счет исполнителя ордера в базовой валюте (доллар), потому что он хочет продать крипту
        Account executerOrderAccount = accountService.getAccountByUserIdAndCurrency(executorOrder.getId(), order.getCurrency().getCode()).orElseThrow(); //счет исполнителя ордера в валюте ордера - сейчас Биток
        OperationType operationType = order.getType();

        cancelOrder(order.getId());
        BigDecimal transferAmount;

        if (operationType.equals(OperationType.BUY)){

            if(executerOrderAccount.getBalance().compareTo(order.getAmount()) >= 0) {
                transferAmount = order.getAmount();
                order.setStatus(OrderStatus.DONE);
            }
            else {
                transferAmount = executerOrderAccount.getBalance();
                order.setStatus(OrderStatus.ACTIVE);
                order.setAmount(order.getAmount().subtract(transferAmount));
            }
            operationService.transfer(ownerBasicAccount.getId(), executerBasicAccount.getId(), transferAmount.multiply(order.getRateValue()));// с долларового счета владельца ордера переводим бабло на долларовый счет исполнителя ордера
            operationService.transfer(executerOrderAccount.getId(), ownerOrderAccount.getId(), transferAmount);
            operationService.addOrderOperation(ownerOrder, executorOrder, order, transferAmount);
            accountService.returnPartOrder(ownerOrderAccount, order.getAmount());

        }
        else {
            if(executerBasicAccount.getBalance().compareTo(order.getAmount().multiply(order.getRateValue())) >= 0) {
                transferAmount = order.getAmount();
                order.setStatus(OrderStatus.DONE);
            }
            else {
                transferAmount = executerBasicAccount.getBalance().divide(order.getRateValue(), 2, RoundingMode.HALF_DOWN).subtract(BigDecimal.valueOf(0.01));
                order.setStatus(OrderStatus.ACTIVE);
                order.setAmount(order.getAmount().subtract(transferAmount));
            }
            operationService.transfer(executerBasicAccount.getId(), ownerBasicAccount.getId(), transferAmount.multiply(order.getRateValue()));// с долларового счета владельца ордера переводим бабло на долларовый счет исполнителя ордера
            operationService.transfer(ownerOrderAccount.getId(), executerOrderAccount.getId(), transferAmount);
            operationService.addOrderOperation(executorOrder, ownerOrder, order, transferAmount);
            accountService.returnPartOrder(ownerOrderAccount, order.getAmount());

        }
        orderRepository.save(order);
    }
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOrderById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        Account orderAccount = accountService.getAccountFromOrder(order);//аккаунт, где заморожены деньги

        BigDecimal orderAmount = order.getAmount();
        orderAccount.setOrderBalance(orderAccount.getOrderBalance().subtract(orderAmount));
        orderAccount.setBalance(orderAccount.getBalance().add(orderAmount));
        accountService.updateAccount(orderAccount.getId(), orderAccount);
        updateOrder(orderId, order);
    }
}
