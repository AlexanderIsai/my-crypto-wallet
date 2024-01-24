package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.dto.AccountAddDTO;
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

    void addNewAccount(AccountAddDTO accountAddDTO);

    List<Account> getAllAccounts();

    List<Account> getAccountsByUser(Long userId);

    List<Account> getAccountsByCurrency(String code);

    Account getAccountByPublicAddress(String address);

    List<Account> getAccountsByBalanceGreaterThan(BigDecimal amount);

    List<Account> getAccountsByBalanceLessThan(BigDecimal amount);

    Boolean existsAccountById(Long id);

    Boolean existsAccountByUserIdAndCurrency(Long userId, String code);

    void updateAccount(Long id, Account account);

    List<Account> getAccountsBetweenAmount(BigDecimal from, BigDecimal to);

    Optional<Account> getAccountByUserIdAndCurrency(Long userId, String code);



}
