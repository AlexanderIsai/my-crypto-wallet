package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * description
 *
 * @author Alexander Isai on 26.01.2024.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/add")
    public void addNewOrder(@RequestBody OrderAddDTO orderAddDTO){
        orderService.addOrder(orderAddDTO);
    }
}
