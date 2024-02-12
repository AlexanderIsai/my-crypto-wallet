package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.dto.OrderShowDTO;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 18.01.2024.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM crypto_orders")
    List<Order> getAllOrders();
    Order findOrderById(Long id);
    List<Order> findOrdersByUserId(Long userId);
    List<Order> findOrdersByCurrencyCode(String code);
    List<Order> findOrdersByUserIdAndCurrencyCode(Long userId, String code);
    List<Order> findOrdersByStatus(OrderStatus status);
    List<Order> findOrdersByUserIdAndStatus(Long userId, OrderStatus status);
    List<Order> findOrdersByStatusAndTypeAndCurrencyCode(OrderStatus status, OperationType type, String code, Sort sort);

    List<Order> findOrdersByStatusAndType(OrderStatus status, OperationType type);


}
