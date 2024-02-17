package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.TotalUserBalance;

import java.math.BigDecimal;

/**
 * The AccountManagerService interface provides a set of methods for managing an application's main account. These methods include.
 * Account deposits, withdrawals, buying and selling cryptocurrencies.
 * This interface is intended to be used by services that implement logic for
 * business transactions related to user accounts, such as increasing a balance when funding an account
 * or decreasing the balance when withdrawing funds.
 *
 * @author Alexander Isai
 * @version 14.02.2024
 */
public interface AccountManagerService {

    /**
     * Deposits the application account by the specified amount in the specified currency.
     *
     * @param code The currency code for which the deposit is being made.
     * @param amount The deposit amount.
     */
    void depositManager(String code, BigDecimal amount);

    /**
     * Withdraws a specified amount from the application account for a given currency.
     *
     * @param code The currency code from which the withdrawal is being made.
     * @param amount The withdrawal amount.
     */
    void withdrawManager(String code, BigDecimal amount);

    /**
     * Executes the purchase of cryptocurrency for the amount specified by the user.
     *
     * @param code The currency code that the user wants to buy.
     * @param amount The amount in currency for which the purchase is made.
     */
    void buyManager(String code, BigDecimal amount);

    /**
     * Executes the sale of cryptocurrency for the amount specified by the user.
     *
     * @param code The currency code that the user wants to sell.
     * @param amount The amount in currency being sold.
     */
    void sellManager(String code, BigDecimal amount);

}