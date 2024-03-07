package de.telran.mycryptowallet.dto.orderDTO;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for displaying order information. It includes the currency code,
 * type of operation, and the current status of the order.
 *
 * @author Alexander Isai
 * @version 11.02.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderShowDTO {

    /**
     * The currency code associated with the order. Must not be blank.
     */
    @NotBlank(message = "Currency code must not be blank")
    private String currencyCode;

    /**
     * The type of operation for the order, such as BUY or SELL. Cannot be null.
     */
    @NotNull(message = "Operation type must not be null")
    private OperationType operationType;

    /**
     * The current status of the order, indicating whether it is ACTIVE, CANCELLED, DONE, or AUTO. Cannot be null.
     */
    @NotNull(message = "Order status must not be null")
    private OrderStatus orderStatus;
}




