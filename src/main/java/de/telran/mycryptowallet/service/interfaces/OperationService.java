package de.telran.mycryptowallet.service.interfaces;
import de.telran.mycryptowallet.dto.operationDTO.OperationAddDTO;
import de.telran.mycryptowallet.dto.operationDTO.OperationTransferDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;

import java.math.BigDecimal;

/**
 * Defines the operations related to financial transactions within the cryptocurrency wallet application.
 * This interface outlines the methods for handling various types of operations such as exchange, order processing, and funds transfer.
 */
public interface OperationService {

    /**
     * Creates and returns an exchange operation for a user involving a specific cryptocurrency.
     *
     * @param user The user performing the exchange operation.
     * @param operationAddDTO (currency code, amount, operation Type)
     * @return An Operation object representing the exchange operation.
     */
    Operation getExchangeOperation(User user, OperationAddDTO operationAddDTO);

    /**
     * Processes an order operation between the order owner and the order executor.
     *
     * @param orderOwner The user who owns the order.
     * @param orderExecutor The user executing the order.
     * @param order The order being executed.
     * @param amount The amount involved in the order operation.
     */
    void addOrderOperation(User orderOwner, User orderExecutor, Order order, BigDecimal amount);

    /**
     * Updates account balances based on the provided operation.
     *
     * @param operation The operation to process for cash flow adjustments.
     */
    void cashFlow(Operation operation);

    /**
     * Processes a buy operation, updating account balances accordingly.
     *
     * @param operation The buy operation to process.
     */
    void buy(Operation operation);

    /**
     * Processes a sell operation, updating account balances accordingly.
     *
     * @param operation The sell operation to process.
     */
    void sell(Operation operation);

    /**
     * Transfers a specified amount from one account to another.
     *
     * @param from The account from which funds are being transferred.
     * @param to The account to which funds are being transferred.
     * @param amount The amount of funds to transfer.
     */
    void transferByOrder(Account from, Account to, BigDecimal amount);
    //TODO добавить админские методы (статистика и т.п.)

    Operation getOperationById(Long id);

    void transferBetweenUsers(User user, OperationTransferDTO operationTransferDTO);

}
