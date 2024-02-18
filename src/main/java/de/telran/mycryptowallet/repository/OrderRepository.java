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
 * Repository interface for handling operations related to {@link Order} entities.
 * Provides methods for retrieving and manipulating orders within the database, including fetching all orders,
 * orders by ID, by user, by currency code, by status, and by combinations of user, status, type, and currency code.
 *
 * @see Order Entity representing a transaction order.
 * @see JpaRepository Spring Data JPA repository for generic CRUD operations.
 * @author Alexander Isai
 * @version 18.01.2024
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves all orders from the database.
     *
     * @return a list of all {@link Order}.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM crypto_orders")
    List<Order> getAllOrders();

    /**
     * Finds an order by its ID.
     * @param id the ID of the order.
     * @return the {@link Order} with the specified ID.
     */
    Order findOrderById(Long id);

    /**
     * Finds orders associated with a specific user ID.
     * @param userId the user's ID.
     * @return a list of {@link Order} belonging to the specified user.
     */
    List<Order> findOrdersByUserId(Long userId);

    /**
     * Finds orders by their associated currency code.
     * @param code the currency code.
     * @return a list of {@link Order} with the given currency code.
     */
    List<Order> findOrdersByCurrencyCode(String code);

    /**
     * Finds orders for a specific user and currency code.
     * @param userId the user's ID.
     * @param code the currency code.
     * @return a list of {@link Order} for the specified user and currency code.
     */
    List<Order> findOrdersByUserIdAndCurrencyCode(Long userId, String code);

    /**
     * Finds orders by their status.
     * @param status the order status.
     * @return a list of {@link Order} with the given status.
     */
    List<Order> findOrdersByStatus(OrderStatus status);

    /**
     * Finds orders for a specific user with a specific status.
     * @param userId the user's ID.
     * @param status the order status.
     * @return a list of {@link Order} for the specified user with the given status.
     */
    List<Order> findOrdersByUserIdAndStatus(Long userId, OrderStatus status);

    /**
     * Finds orders filtered by status, type, and currency code, with sorting.
     * @param status the order status.
     * @param type the operation type.
     * @param code the currency code.
     * @param sort the sort criteria.
     * @return a list of {@link Order} matching the criteria.
     */
    List<Order> findOrdersByStatusAndTypeAndCurrencyCode(OrderStatus status, OperationType type, String code, Sort sort);

    /**
     * Finds orders filtered by status and type.
     * @param status the order status.
     * @param type the operation type.
     * @return a list of {@link Order} matching the criteria.
     */
    List<Order> findOrdersByStatusAndType(OrderStatus status, OperationType type);
}
