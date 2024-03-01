package de.telran.mycryptowallet.controller;
import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.dto.OrderShowDTO;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for managing orders within the cryptocurrency wallet application.
 * Provides endpoints for adding, viewing, executing, and canceling orders,
 * as well as querying orders by various criteria such as status or specific user requests.
 * @author Alexander Isai on 26.01.2024.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ActiveUserService activeUserService;

    /**
     * Endpoint for creating a new order. It captures the order details from the request body
     * and uses the active user's information to add a new order to the system.
     *
     * @param orderAddDTO Data Transfer Object containing the new order details.
     */
    @PostMapping(value = "/add")
    @Operation(summary = "Add new order", description = "Create new order")
    public void addNewOrder(@RequestBody OrderAddDTO orderAddDTO) {
        orderService.addOrder(activeUserService.getActiveUser(), orderAddDTO.getCurrencyCode(), orderAddDTO.getOperationType(),
                orderAddDTO.getAmount(), orderAddDTO.getOrderRate());
    }
    /**
     * Endpoint to retrieve all orders in the system. It returns a list of orders without filtering.
     *
     * @return List of all orders in the system.
     */
    @GetMapping(value = "/all-orders")
    @Operation(summary = "Show all orders", description = "Shows information about all orders")
    public List<Order> showAllOrders(){
        return orderService.getAllOrders();
    }
    /**
     * Endpoint to fetch a specific order by its ID. It returns the order details if found,
     * or a not found response if the order does not exist.
     *
     * @param id The unique identifier of the order to retrieve.
     * @return ResponseEntity containing the order or a not found status.
     */
    @GetMapping(value = "/{id}")
    @Operation(summary = "Show order by ID", description = "Returns order information by ID")
    public ResponseEntity<Order> showOrderById(@PathVariable (value = "id") Long id){
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }
    /**
     * Endpoint for executing an order by its ID. The order is executed by the active user.
     *
     * @param id The unique identifier of the order to execute.
     */
    @GetMapping(value = "/use")
    @Operation(summary = "Execute order", description = "Execution of a specified order by ID by the active user")
    public void executeOrder(@RequestParam(name = "id") Long id) {
        orderService.executeOrder(id);
    }
    /**
     * Endpoint to cancel an order by its ID. The order is marked as cancelled in the system.
     *
     * @param id The unique identifier of the order to cancel.
     */
    @GetMapping(value = "/cancel")
    @Operation(summary = "Cancel order", description = "Cancel order by ID")
    public void cancelOrder(@RequestParam(name = "id") Long id){
        orderService.cancelOrder(id);
    }
    /**
     * Endpoint to retrieve orders filtered by their status. It returns orders matching the specified status.
     *
     * @param status The status to filter orders by.
     * @return List of orders with the specified status.
     */
    @GetMapping(value="/status")
    @Operation(summary = "Show all orders by status", description = "Returns detailed information about orders with the specified status")
    public List<Order> showOrdersByStatus(@RequestParam(name = "status") OrderStatus status){
        return orderService.getOrdersByStatus(status);
    }
    /**
     * Endpoint to retrieve orders based on a specific request criteria encapsulated in a DTO.
     *
     * @param showDTO Data Transfer Object containing the request criteria.
     * @return List of orders matching the request criteria.
     */
    @PostMapping(value = "/by-request")
    @Operation(summary = "Show all orders by request", description = "Returns detailed information about warrants for a given request")
    public List<Order> showOrderByRequest(@RequestBody OrderShowDTO showDTO){
        return orderService.getOrdersByStatusTypeCurrency(showDTO.getOrderStatus(), showDTO.getOperationType(), showDTO.getCurrencyCode());
    }
    /**
     * Endpoint to retrieve orders belonging to the active user. It returns a list of the active user's orders.
     *
     * @return List of orders associated with the active user.
     */
    @GetMapping(value = "/my")
    @Operation(summary = "Show orders of the active user", description = "Returns detailed information about the active user's orders")
    public List<Order> showMyOrders(){
        return orderService.getUsersOrders(activeUserService.getActiveUser().getId());
    }

}
