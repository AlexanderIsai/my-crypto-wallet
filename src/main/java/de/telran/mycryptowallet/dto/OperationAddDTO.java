package de.telran.mycryptowallet.dto;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 The use of this DTO facilitates the clean and efficient transfer
 * of data from the user interface to the backend services, ensuring that only relevant data is
 * processed for the execution of operations.
 * <p>The {@code currencyCode} attribute specifies the currency in which the operation is to be
 * conducted. </p>
 * <p>The {@code amount} attribute represents the monetary value involved in the operation.</p>
 * <p>The {@code type} attribute identifies the type of operation being requested (e.g., BUY, SELL,
 * DEPOSIT, WITHDRAW). </p>
 * @author Alexander Isai
 * @version 24.01.2024
 */
@Data
public class OperationAddDTO {

    /**
     * The code of the currency involved in the operation.
     * Must not be null or empty. Also, can add pattern validation if there is a specific format for currency codes.
     */
    @NotBlank(message = "Currency code cannot be empty")
    private String currencyCode;

    /**
     * The amount of currency to be processed in the operation.
     * Must be a positive number.
     */
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    private BigDecimal amount;

    /**
     * The type of financial operation to be performed.
     * Must not be null.
     */
    @NotNull(message = "Operation type cannot be null")
    private OperationType type;
}
