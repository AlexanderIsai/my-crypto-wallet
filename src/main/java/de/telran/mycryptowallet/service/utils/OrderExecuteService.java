package de.telran.mycryptowallet.service.utils;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.OperationService;
import de.telran.mycryptowallet.service.interfaces.OrderService;
import de.telran.mycryptowallet.service.interfaces.RateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 12.02.2024.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderExecuteService {

    private final OrderService orderService;
    private final RateService rateService;
    private final AccountService accountService;
    private final OperationService operationService;

    @Scheduled(cron = "0 */6 * * * *")
    public void executeBuyOrders() {
        List<Order> ordersBuy = orderService.getOrdersByStatusAndType(OrderStatus.ACTIVE, OperationType.BUY);
        executeOrders(ordersBuy, OperationType.BUY);
    }

    @Scheduled(cron = "0 */6 * * * *")
    @Transactional
    public void executeSellOrders() {
        List<Order> ordersSell = orderService.getOrdersByStatusAndType(OrderStatus.ACTIVE, OperationType.SELL);
        executeOrders(ordersSell, OperationType.SELL);
    }

    private void executeOrders(List<Order> orders, OperationType operationType) {
        for (Order order : orders) {
            Rate currentRate = rateService.getFreshRate(order.getCurrency().getCode());
            BigDecimal targetRate = operationType == OperationType.BUY ? currentRate.getSellRate() : currentRate.getBuyRate();

            if ((operationType == OperationType.BUY && order.getRateValue().compareTo(targetRate) >= 0) ||
                    (operationType == OperationType.SELL && order.getRateValue().compareTo(targetRate) <= 0)) {
                performOrderOperation(order, operationType, targetRate);
            }
        }
    }

    private void performOrderOperation(Order order, OperationType operationType, BigDecimal rateValue) {
        Operation operation = new Operation();

        User user = order.getUser();
        operation.setUser(user);
        operation.setCurrency(order.getCurrency());

        Account account = accountService.getAccountByUserIdAndCurrency(user.getId(), order.getCurrency().getCode());
        operation.setAccount(account);
        operation.setRateValue(rateValue);
        operation.setAmount(order.getAmount());
        operation.setType(operationType);

        orderService.cancelOrder(order.getId());
        order.setStatus(OrderStatus.AUTO);
        orderService.updateOrder(order.getId(), order);
        operationService.cashFlow(operation);
    }
}