package de.telran.mycryptowallet.service;

import de.telran.mycryptowallet.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
public interface AccountService {

    void addNewAccount(Account account);

    List<Account> getAllAccounts();

    List<Account> getAccountsByUser(Long userId);

    List<Account> getAccountsByCurrency(String code);

    Optional<Account> getAccountByIDAndUserId(Long accountID, Long userId);

    Account getAccountByPublicAddress(String address);

    List<Account> getAccountsByBalanceGreaterThan(BigDecimal amount);

    List<Account> getAccountsByBalanceLessThan(BigDecimal amount);

    Boolean existsAccountById(Long id);

    Boolean existsAccountByUserId(Long userId);

    void updateAccount(Long id, Account account);

    List<Account> getAccountsBetweenAmount(BigDecimal from, BigDecimal to);

    Optional<Account> getAccountByUserIdAndCurrency(Long userId, String code);
}
