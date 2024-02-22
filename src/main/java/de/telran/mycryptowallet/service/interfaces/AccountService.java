package de.telran.mycryptowallet.service.interfaces;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * description
 * Provides service operations for managing accounts within the cryptocurrency wallet application.
 * @author Alexander Isai on 22.01.2024.
 */
public interface AccountService {

    /**
     * Adds a new account for a user with the specified currency code.
     *
     * @param user the user to whom the account will be added
     * @param code the currency code for the new account
     */
    void addNewAccount(User user, String code);

    /**
     * Retrieves all accounts in the system.
     *
     * @return a list of all accounts
     */
    List<Account> getAllAccounts();

    /**
     * Retrieves accounts associated with a specific user.
     *
     * @param userId the user's ID
     * @return a list of accounts for the specified user
     */
    List<Account> getAccountsByUser(Long userId);

    /**
     * Retrieves accounts associated with a specific currency.
     *
     * @param code the currency code
     * @return a list of accounts for the specified currency
     */
    List<Account> getAccountsByCurrency(String code);

    /**
     * Retrieves an account by its public address.
     *
     * @param address the public address of the account
     * @return the account with the specified public address
     */
    Account getAccountByPublicAddress(String address);

    /**
     * Adds accounts with a new currency to all users.
     *
     * @param code the currency code for the new accounts
     */
    void addAccountsWithNewCurrency(String code);

    /**
     * Updates an existing account.
     *
     * @param id the ID of the account to update
     * @param account the updated account information
     */
    void updateAccount(Long id, Account account);

    /**
     * Retrieves an account for a user with a specific currency.
     *
     * @param userId the user's ID
     * @param code the currency code
     * @return an Optional containing the account if found
     */
    Optional<Account> getAccountByUserIdAndCurrency(Long userId, String code);

    /**
     * Creates default accounts for a new user.
     *
     * @param user the user for whom to create accounts
     */
    void createUserAccounts(User user);

    /**
     * Retrieves an account by its ID.
     *
     * @param id the account's ID
     * @return the account with the specified ID
     */
    Account getAccountById(Long id);

    /**
     * Retrieves accounts with a balance greater than a specified amount.
     *
     * @param amount the amount to compare against
     * @return a list of accounts with a balance greater than the specified amount
     */
    List<Account> getAccountsByBalanceGreaterThan(BigDecimal amount);

    /**
     * Retrieves accounts with a balance less than a specified amount.
     *
     * @param amount the amount to compare against
     * @return a list of accounts with a balance less than the specified amount
     */
    List<Account> getAccountsByBalanceLessThan(BigDecimal amount);

    /**
     * Checks if an account exists by its ID.
     *
     * @param id the account's ID
     * @return true if the account exists, false otherwise
     */
    Boolean existsAccountById(Long id);

    /**
     * Checks if an account exists for a user with a specific currency.
     *
     * @param userId the user's ID
     * @param code the currency code
     * @return true if the account exists, false otherwise
     */
    Boolean existsAccountByUserIdAndCurrency(Long userId, String code);

    /**
     * Retrieves accounts with a balance between two amounts.
     *
     * @param from the minimum amount
     * @param to the maximum amount
     * @return a list of accounts with a balance between the specified amounts
     */
    List<Account> getAccountsBetweenAmount(BigDecimal from, BigDecimal to);

    void resetAllAccounts();
}