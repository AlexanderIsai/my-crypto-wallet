package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.exceptions.NotActiveOrder;

import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 25.01.2024.
 */
public interface OrderService {

    void addOrder(OrderAddDTO orderDTO);

    List<Order> getAllOrders();

    void updateOrder(Long id, Order order);

    Order getOrderById(Long id);

    void executeOrder(Long orderId) throws NotActiveOrder;

    void cancelOrder(Long orderId);


}
