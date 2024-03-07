package de.telran.mycryptowallet.dto.orderDTO;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) used for adding a new order within the cryptocurrency wallet application.
 * It contains necessary information required to create an order, including the type of operation,
 * the currency code, the amount of currency involved, and the rate at which the order should be executed.
 * Ensuring accurate data validation is crucial for maintaining the integrity and reliability of the system's operations.
 *
 * @author Alexander Isai
 * @version 25.01.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAddDTO {

    /**
     * The currency code associated with the order. Must not be blank.
     */
    @NotBlank(message = "Currency code is required.")
    private String currencyCode;

    /**
     * The type of operation for the order (e.g., BUY, SELL). Cannot be null.
     */
    @NotNull(message = "Operation type is required.")
    private OperationType type;

    /**
     * The amount of currency to be processed in the order. Must be a positive value.
     */
    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.")
    private BigDecimal amount;

    /**
     * The rate at which the order should be executed. Must be a positive value.
     */
    @NotNull(message = "Order rate is required.")
    @DecimalMin(value = "0.01", message = "Order rate must be greater than 0.")
    private BigDecimal orderRate;
}