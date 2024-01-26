package de.telran.mycryptowallet.dto;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 25.01.2024.
 */
@Data
public class OrderAddDTO {

    private String currencyCode;
    private OperationType operationType;
    private BigDecimal amount;
    private OrderStatus orderStatus;
    private BigDecimal orderRate;
}
