package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 24.01.2024.
 */
public interface OperationService {

    void addOperation(OperationAddDTO operationAddDTO);
    void cashFlow(OperationType type, Long userId, String code, BigDecimal amount);
    void buy(Long userId, String code, BigDecimal amount);
    void sell(Long userId, String code, BigDecimal amount);


}
