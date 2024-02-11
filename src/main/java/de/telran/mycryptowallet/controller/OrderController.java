package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.dto.OrderShowDTO;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.exceptions.NotActiveOrderException;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final ActiveUserService activeUserService;

    @PostMapping(value = "/add")
    public void addNewOrder(@RequestBody OrderAddDTO orderAddDTO) {
        orderService.addOrder(orderAddDTO);
    }

    @GetMapping(value = "/all-orders")
    public List<Order> showAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> showOrderById(@PathVariable (value = "id") Long id){
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }
    @GetMapping(value = "/use")
    public void executeOrder(@RequestParam(name = "id") Long id) {
        orderService.executeOrder(id);
    }

    @GetMapping(value = "/cancel")
    public void cancelOrder(@RequestParam(name = "id") Long id){
        orderService.cancelOrder(id);
    }

    @GetMapping(value="/status")
    public List<Order> showOrdersByStatus(@RequestParam(name = "status") OrderStatus status){
        return orderService.getOrdersByStatus(status);
    }

    @GetMapping(value = "/by-request")
    public List<Order> showOrderByRequest(@RequestBody OrderShowDTO showDTO){
        return orderService.getOrdersByStatusTypeCurrency(showDTO.getOrderStatus(), showDTO.getOperationType(), showDTO.getCurrencyCode());
    }

    @GetMapping(value = "/my")
    public List<Order> showMyOrders(){
        return orderService.getUsersOrders(activeUserService.getActiveUser().getId());
    }


}
