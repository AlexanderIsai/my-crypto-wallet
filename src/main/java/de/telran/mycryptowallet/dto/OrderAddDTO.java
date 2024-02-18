package de.telran.mycryptowallet.dto;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Alexander Isai
 * @version 25.01.2024
 */
@Data
public class OrderAddDTO {

    /**
     * Currency code for the order.
     */
    private String currencyCode;

    /**
     * Type of operation (e.g., BUY, SELL) for the order.
     */
    private OperationType operationType;

    /**
     * Monetary value for the order.
     */
    private BigDecimal amount;

    /**
     * Specified rate for the order execution.
     */
    private BigDecimal orderRate;
}