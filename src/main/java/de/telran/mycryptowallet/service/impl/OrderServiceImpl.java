package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.repository.OrderRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final RateService rateService;
    private final CurrencyService currencyService;
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
}
