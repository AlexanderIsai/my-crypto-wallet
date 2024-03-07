package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.dto.orderDTO.OrderAddDTO;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 25.01.2024.
 */
public interface OrderService {

    void addOrder(User user, OrderAddDTO orderAddDTO);

    List<Order> getAllOrders();

    List<Order> getUsersOrders(Long userId);

    void updateOrder(Long id, Order order);

    Order getOrderById(Long id);

    void executeOrder(Long orderId);

    void cancelOrder(Long orderId);

    List<Order> getOrdersByStatus(OrderStatus orderStatus);

    List<Order> getOrdersByStatusTypeCurrency(OrderStatus status, OperationType type, String code);

    List<Order> getOrdersByStatusAndType(OrderStatus status, OperationType type);

    BigDecimal getOrderAmount(Order order);

    void cancelAllOrders();

}
