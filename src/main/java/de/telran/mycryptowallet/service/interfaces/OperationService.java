package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Defines the operations related to financial transactions within the cryptocurrency wallet application.
 * This interface outlines the methods for handling various types of operations such as exchange, order processing, and funds transfer.
 */
public interface OperationService {

    /**
     * Creates and returns an exchange operation for a user involving a specific cryptocurrency.
     *
     * @param user The user performing the exchange operation.
     * @param code The code of the cryptocurrency involved.
     * @param amount The amount of cryptocurrency to be exchanged.
     * @param type The type of exchange operation (BUY or SELL).
     * @return An Operation object representing the exchange operation.
     */
    Operation getExchangeOperation(User user, String code, BigDecimal amount, OperationType type);

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
    void transfer(Account from, Account to, BigDecimal amount);
    //TODO добавить админские методы (статистика и т.п.)

    Optional<Operation> getOperationById(Long id);

}
