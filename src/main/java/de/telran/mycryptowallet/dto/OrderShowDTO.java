package de.telran.mycryptowallet.dto;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import lombok.Data;

/**
 * description
 *
 * @author Alexander Isai on 11.02.2024.
 */
@Data
public class OrderShowDTO {

    private String currencyCode;
    private OperationType operationType;
    private OrderStatus orderStatus;
}
