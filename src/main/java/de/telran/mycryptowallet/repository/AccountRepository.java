package de.telran.mycryptowallet.repository;
import de.telran.mycryptowallet.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;



/**
 * Repository interface for handling CRUD operations on {@link Account} entities within the database.
 * Offers methods for retrieving, searching, and checking the existence of accounts based on various criteria such as user ID, currency code, balance, and public address.
 *
 * @see Account Entity representing a user's account holding a specific currency.
 * @see JpaRepository Spring Data JPA repository for generic CRUD operations.
 * @author Alexander Isai
 * @version 18.01.2024
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Retrieves all account entities from the database.
     *
     * @return a list of all {@link Account} entities.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM crypto_accounts")
    List<Account> getAllAccounts();

    /**
     * Finds accounts belonging to a specific user.
     *
     * @param userId the ID of the user.
     * @return a list of matching {@link Account} entities.
     */
    List<Account> findAccountsByUserId(Long userId);

    /**
     * Finds accounts associated with a specific currency code.
     *
     * @param code the code of the currency.
     * @return a list of matching {@link Account} entities.
     */
    List<Account> findAccountsByCurrencyCode(String code);

    /**
     * Finds an account by its unique ID.
     *
     * @param id the ID of the account.
     * @return the found {@link Account} entity.
     */
    Account findAccountById(Long id);

    /**
     * Finds an account by its public address.
     *
     * @param address the public address of the account.
     * @return the found {@link Account} entity.
     */
    Account findAccountByPublicAddress(String address);

    /**
     * Finds accounts with a balance greater than the specified amount.
     *
     * @param amount the minimum balance amount.
     * @return a list of matching {@link Account} entities.
     */
    List<Account> findAccountsByBalanceGreaterThan(BigDecimal amount);

    /**
     * Finds accounts with a balance less than the specified amount.
     *
     * @param amount the maximum balance amount.
     * @return a list of matching {@link Account} entities.
     */
    List<Account> findAccountsByBalanceLessThan(BigDecimal amount);

    /**
     * Checks if an account exists by its ID.
     *
     * @param id the ID of the account.
     * @return true if the account exists, false otherwise.
     */
    Boolean existsAccountById(Long id);

    /**
     * Checks if an account exists for a specific user and currency code.
     *
     * @param userId the ID of the user.
     * @param code the code of the currency.
     * @return true if the account exists, false otherwise.
     */
    Boolean existsAccountByUserIdAndCurrencyCode(Long userId, String code);

    /**
     * Retrieves accounts with a balance within a specified range.
     *
     * @param from the minimum balance amount.
     * @param to the maximum balance amount.
     * @return a list of matching {@link Account} entities.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM crypto_accounts WHERE balance BETWEEN :from AND :to")
    List<Account> getAccountsBetween(@Param(value = "from") BigDecimal from, @Param(value = "to") BigDecimal to);

    /**
     * Finds an account for a specific user and currency code.
     *
     * @param userId the ID of the user.
     * @param code the code of the currency.
     * @return the found {@link Account} entity.
     */
    Account findAccountByUserIdAndCurrencyCode(Long userId, String code);

    @Query(nativeQuery = true, value = "SELECT SUM(balance + order_balance) FROM crypto_accounts WHERE currency = :code")
    BigDecimal getSumAccountByCode(String code);
}