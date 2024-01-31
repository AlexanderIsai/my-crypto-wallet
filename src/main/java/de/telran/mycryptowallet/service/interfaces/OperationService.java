package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 24.01.2024.
 */
public interface OperationService {

    void addExchangeOperation(OperationAddDTO operationAddDTO) throws NotEnoughFundsException;
    void addOrderOperation(User orderOwner, User orderExecutor, Order order);
    void cashFlow(Operation operation) throws NotEnoughFundsException;
    void buy(Operation operation) throws NotEnoughFundsException;
    void sell(Operation operation) throws NotEnoughFundsException;
    void transfer(Long fromId, Long toId, BigDecimal amount);


}
