package de.telran.mycryptowallet.dto;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for displaying order information. It includes the currency code,
 * type of operation, and the current status of the order.
 *
 * @author Alexander Isai
 * @version 11.02.2024
 */
@Data
public class OrderShowDTO {

    /**
     * The currency code associated with the order.
     */
    private String currencyCode;

    /**
     * The type of operation for the order, such as BUY or SELL.
     */
    private OperationType operationType;

    /**
     * The current status of the order, indicating whether it is ACTIVE, CANCELLED, DONE, or AUTO.
     */
    private OrderStatus orderStatus;
}




