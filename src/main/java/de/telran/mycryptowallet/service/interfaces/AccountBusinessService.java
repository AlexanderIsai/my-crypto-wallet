package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Provides business logic for account operations including deposits, withdrawals,
 * reservation for orders, and calculation of total and profit balances.
 * <p>
 * This interface defines methods for handling money transactions on accounts,
 * managing reserves for trading orders, and aggregating user balance information.
 *
 * @author Alexander Isai
 * @version 1.0
 * @since 2024-02-13
 */
public interface AccountBusinessService {

    /**
     * Deposits a specified amount into the given account.
     *
     * @param account the account to deposit into
     * @param amount  the amount to deposit
     */
    void deposit(Account account, BigDecimal amount);

    /**
     * Withdraws a specified amount from the given account.
     *
     * @param account the account to withdraw from
     * @param amount  the amount to withdraw
     */
    void withdraw(Account account, BigDecimal amount);

    /**
     * Reserves a specified amount for an order based on the user, currency code,
     * operation type, amount, and rate.
     *
     * @param user   the user for whom the order is being placed
     * @param code   the currency code
     * @param type   the operation type (e.g., BUY or SELL)
     * @param amount the amount to reserve
     * @param rate   the rate at which the order is placed
     */
    void reserveForOrder(User user, String code, OperationType type, BigDecimal amount, BigDecimal rate);

    /**
     * Retrieves the account associated with a given order.
     *
     * @param order the order for which to retrieve the account
     * @return the account associated with the order
     */
    Account getAccountFromOrder(Order order);

    /**
     * Returns a part of an order to the account, effectively reversing a
     * portion of a previous reservation.
     *
     * @param account the account to return the order part to
     * @param amount  the amount to return
     */
    void returnPartOrder(Account account, BigDecimal amount);

    /**
     * Calculates the total balance for a given user, including all accounts.
     *
     * @param userId the ID of the user
     * @return the total balance across all accounts for the user
     */
    TotalUserBalance getTotalUserBalance(Long userId);

    /**
     * Calculates the total balance across all users and accounts.
     *
     * @return the total balance across all accounts
     */
    TotalUserBalance getTotalBalance();

    /**
     * Calculates and shows the profit for the app.
     *
     * @return the total profit
     */
    TotalUserBalance showProfit();
}
