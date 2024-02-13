package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.repository.OrderRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import de.telran.mycryptowallet.service.utils.validators.OrderValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
    private final OrderValidator orderValidator;
    private final AccountValidator accountValidator;
    private final AccountBusinessService accountBusinessService;
    private final static int SCALE = 2;

    @Override
    @Transactional
    public void addOrder(User user, String code, OperationType type, BigDecimal amount, BigDecimal rate) {
        Order order = new Order();

        order.setUser(user);

        Currency orderCurrency = currencyService.getCurrencyByCode(code);
        order.setCurrency(orderCurrency);

        order.setRateValue(rate);
        accountValidator.isCorrectNumber(amount);
        order.setAmount(amount);

        order.setType(type);
        order.setStatus(OrderStatus.ACTIVE);

        accountBusinessService.reserveForOrder(user, code, type, amount, rate);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    @Override
    public List<Order> getUsersOrders(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
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
        Order order = getOrderById(orderId);// нужный ордер
        orderValidator.isOrderActive(order);
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
            if(order.getStatus().equals(OrderStatus.ACTIVE)) {
                accountBusinessService.returnPartOrder(ownerOrderAccount, order.getAmount());
            }
        }
        else {
            if(executerBasicAccount.getBalance().compareTo(order.getAmount().multiply(order.getRateValue())) >= 0) {
                transferAmount = order.getAmount();
                order.setStatus(OrderStatus.DONE);
            }
            else {
                transferAmount = executerBasicAccount.getBalance().divide(order.getRateValue(), SCALE, RoundingMode.HALF_DOWN).subtract(BigDecimal.valueOf(0.01));
                order.setStatus(OrderStatus.ACTIVE);
                order.setAmount(order.getAmount().subtract(transferAmount));
            }
            operationService.transfer(executerBasicAccount.getId(), ownerBasicAccount.getId(), transferAmount.multiply(order.getRateValue()));// с долларового счета исполнителя ордера переводим бабло на долларовый счет владельца ордера
            operationService.transfer(ownerOrderAccount.getId(), executerOrderAccount.getId(), transferAmount);
            operationService.addOrderOperation(executorOrder, ownerOrder, order, transferAmount);

            if(order.getStatus().equals(OrderStatus.ACTIVE)) {
                accountBusinessService.returnPartOrder(ownerOrderAccount, order.getAmount()); // переведи остаток на счет
            }
        }
        orderRepository.save(order);
    }
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOrderById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        Account orderAccount = accountBusinessService.getAccountFromOrder(order);//аккаунт, где заморожены деньги

        BigDecimal orderAmount = getOrderAmount(order);
        orderAccount.setOrderBalance(orderAccount.getOrderBalance().subtract(orderAmount));
        orderAccount.setBalance(orderAccount.getBalance().add(orderAmount));
        accountService.updateAccount(orderAccount.getId(), orderAccount);
        updateOrder(orderId, order);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus orderStatus) {
        return orderRepository.findOrdersByStatus(orderStatus);
    }

    @Override
    public List<Order> getOrdersByStatusTypeCurrency(OrderStatus status, OperationType type, String code) {
        Sort sort = type.equals(OperationType.BUY) ? Sort.by(Sort.Direction.DESC, "rateValue") : Sort.by(Sort.Direction.ASC, "rateValue");
        return orderRepository.findOrdersByStatusAndTypeAndCurrencyCode(status, type, code, sort);
    }

    @Override
    public List<Order> getOrdersByStatusAndType(OrderStatus status, OperationType type) {
        return orderRepository.findOrdersByStatusAndType(status, type);
    }

    @Override
    public BigDecimal getOrderAmount(Order order) {
        return order.getType().equals(OperationType.BUY) ? order.getAmount().multiply(order.getRateValue()) : order.getAmount();
    }
}
