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
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Создать новый ордер", description = "Создает новый ордер")
    public void addNewOrder(@RequestBody OrderAddDTO orderAddDTO) {
        orderService.addOrder(activeUserService.getActiveUser(), orderAddDTO.getCurrencyCode(), orderAddDTO.getOperationType(),
                orderAddDTO.getAmount(), orderAddDTO.getOrderRate());
    }

    @GetMapping(value = "/all-orders")
    @Operation(summary = "Показать все ордера", description = "Показывает информацию обо всех ордерах")
    public List<Order> showAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Показать ордер по ID", description = "Возвращает информацию об ордере по ID")
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
    @Operation(summary = "Выполнить ордер", description = "Исполнение активным пользователем заданного ордера по ID")
    public void executeOrder(@RequestParam(name = "id") Long id) {
        orderService.executeOrder(id);
    }

    @GetMapping(value = "/cancel")
    @Operation(summary = "Отменить ордер по ID", description = "Отменяет заданный ордер по ID")
    public void cancelOrder(@RequestParam(name = "id") Long id){
        orderService.cancelOrder(id);
    }

    @GetMapping(value="/status")
    @Operation(summary = "Показать все ордера по статусу", description = "Возвращает подробную информацию об ордерах с заданным статусом")
    public List<Order> showOrdersByStatus(@RequestParam(name = "status") OrderStatus status){
        return orderService.getOrdersByStatus(status);
    }

    @GetMapping(value = "/by-request")
    @Operation(summary = "Показатать список ордеров по запросу", description = "Возвращает подробную информацию об ордерах по заданному запросу")
    public List<Order> showOrderByRequest(@RequestBody OrderShowDTO showDTO){
        return orderService.getOrdersByStatusTypeCurrency(showDTO.getOrderStatus(), showDTO.getOperationType(), showDTO.getCurrencyCode());
    }

    @GetMapping(value = "/my")
    @Operation(summary = "Показать ордера активного пользователя", description = "Возвращает подробную информацию об ордерах активного пользователя")
    public List<Order> showMyOrders(){
        return orderService.getUsersOrders(activeUserService.getActiveUser().getId());
    }
    
}
