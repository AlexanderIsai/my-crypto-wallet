package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 24.01.2024.
 */
public interface OperationService {

    void addExchangeOperation(OperationAddDTO operationAddDTO);
    void addOrderOperation(User orderOwner, User orderExecutor, Order order);
    void cashFlow(Operation operation);
    void buy(Operation operation);
    void sell(Operation operation);
    void transfer(Long fromId, Long toId, BigDecimal amount);


}
