package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.repository.OrderRepository;
import de.telran.mycryptowallet.service.interfaces.*;
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
    public void addOrder(OrderAddDTO orderDTO) {
        Order order = new Order();

        User orderUser = activeUserService.getActiveUser();
        order.setUser(orderUser);

        Currency orderCurrency = currencyService.getCurrencyByCode(orderDTO.getCurrencyCode());
        order.setCurrency(orderCurrency);

        Account orderAccount = accountService.getAccountByUserIdAndCurrency(orderUser.getId(), orderCurrency.getCode()).orElseThrow();
        order.setAccount(orderAccount);

        Rate orderRate = rateService.getFreshRate(orderDTO.getCurrencyCode());
        order.setRate(orderRate);

        order.setAmount(orderDTO.getAmount());

        order.setType(orderDTO.getOperationType());
    }
}
