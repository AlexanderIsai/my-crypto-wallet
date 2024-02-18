package de.telran.mycryptowallet.dto;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
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
 * DEPOSIT, WITHDRAW). This distinction is essential for the system to apply the correct business
 * logic and ensure that the operation complies with the applicable rules and regulations.</p>
 * @author Alexander Isai
 * @version 24.01.2024
 */
@Data
public class OperationAddDTO {

    /**
     * The code of the currency involved in the operation.
     */
    private String currencyCode;

    /**
     * The amount of currency to be processed in the operation.
     */
    private BigDecimal amount;

    /**
     * The type of financial operation to be performed.
     */
    private OperationType type;
}
