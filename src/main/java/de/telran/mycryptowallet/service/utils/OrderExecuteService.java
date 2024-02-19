package de.telran.mycryptowallet.service.utils;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.OperationService;
import de.telran.mycryptowallet.service.interfaces.OrderService;
import de.telran.mycryptowallet.service.interfaces.RateService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public void executeBuyOrders(){
        List<Order> ordersBuy = orderService.getOrdersByStatusAndType(OrderStatus.ACTIVE, OperationType.BUY);
        for (Order order : ordersBuy) {
            if (order.getRateValue().compareTo(rateService.getFreshRate(order.getCurrency().getCode()).getSellRate()) >= 0){
                Operation operation = new Operation();

                User user = order.getUser();
                operation.setUser(user);

                operation.setCurrency(order.getCurrency());

                Account account = accountService.getAccountByUserIdAndCurrency(user.getId(), order.getCurrency().getCode()).orElseThrow();
                operation.setAccount(account);

                Rate rate = rateService.getFreshRate(order.getCurrency().getCode());
                operation.setRateValue(rate.getSellRate());

                operation.setAmount(order.getAmount());

                operation.setType(OperationType.BUY);

                orderService.cancelOrder(order.getId());
                order.setStatus(OrderStatus.AUTO);
                orderService.updateOrder(order.getId(), order);
                operationService.cashFlow(operation);
            }
        }
    }
    @Scheduled(cron = "0 */6 * * * *")
    @Transactional
    public void executeSellOrders(){
        List<Order> ordersSell = orderService.getOrdersByStatusAndType(OrderStatus.ACTIVE, OperationType.SELL);
        for (Order order : ordersSell) {
            if (order.getRateValue().compareTo(rateService.getFreshRate(order.getCurrency().getCode()).getBuyRate()) <= 0){
                Operation operation = new Operation();

                User user = order.getUser();
                operation.setUser(user);

                operation.setCurrency(order.getCurrency());

                Account account = accountService.getAccountByUserIdAndCurrency(user.getId(), order.getCurrency().getCode()).orElseThrow();
                operation.setAccount(account);

                Rate rate = rateService.getFreshRate(order.getCurrency().getCode());
                operation.setRateValue(rate.getBuyRate());

                operation.setAmount(order.getAmount());

                operation.setType(OperationType.SELL);

                orderService.cancelOrder(order.getId());

                order.setStatus(OrderStatus.AUTO);

                orderService.updateOrder(order.getId(), order);

                operationService.cashFlow(operation);
            }
        }
    }
}
