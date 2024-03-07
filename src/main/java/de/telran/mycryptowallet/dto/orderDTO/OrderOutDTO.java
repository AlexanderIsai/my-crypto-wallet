package de.telran.mycryptowallet.dto.orderDTO;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * description
 *
 * @author Alexander Isai on 04.03.2024.
 */
@Data
@AllArgsConstructor
public class OrderOutDTO {

    private BigInteger id;
    private String code;
    private BigDecimal rateValue;
    private BigDecimal amount;
    private OperationType type;
    private OrderStatus status;
}
