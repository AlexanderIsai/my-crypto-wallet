package de.telran.mycryptowallet.dto;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 24.01.2024.
 */
@Data
public class OperationAddDTO {

    private String currencyCode;
    private BigDecimal amount;
    private OperationType type;
}
