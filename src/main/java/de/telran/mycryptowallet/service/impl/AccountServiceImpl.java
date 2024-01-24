package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.AccountAddDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.repository.CurrencyRepository;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.utils.PublicAddressGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    public final AccountRepository accountRepository;
    public final CurrencyService currencyService;
    public final ActiveUserService activeUserService;
    @Override
    public void addNewAccount(AccountAddDTO accountAddDTO) {
        Account account = new Account();
        account.setUser(activeUserService.getActiveUser());
        account.setPublicAddress(PublicAddressGenerator.generatePublicAddress(accountAddDTO.getCurrencyCode()));
        account.setCurrency(currencyService.getCurrencyByCode(accountAddDTO.getCurrencyCode()));
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }

    @Override
    public List<Account> getAccountsByUser(Long userId) {
        return accountRepository.findAccountsByUserId(userId);
    }

    @Override
    public List<Account> getAccountsByCurrency(String code) {
        return accountRepository.findAccountsByCurrencyCode(code);
    }

    @Override
    public Account getAccountByPublicAddress(String address) {
        return accountRepository.findAccountByPublicAddress(address);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findAccountById(id);
    }

    @Override
    public List<Account> getAccountsByBalanceGreaterThan(BigDecimal amount) {
        return accountRepository.findAccountsByBalanceGreaterThan(amount);
    }

    @Override
    public List<Account> getAccountsByBalanceLessThan(BigDecimal amount) {
        return accountRepository.findAccountsByBalanceLessThan(amount);
    }

    @Override
    public Boolean existsAccountById(Long id) {
        return accountRepository.existsAccountById(id);
    }

    @Override
    public Boolean existsAccountByUserIdAndCurrency(Long userId, String code) {
        return accountRepository.existsAccountByUserIdAndCurrencyCode(userId, code);
    }

    @Override
    public void updateAccount(Long id, Account account) {
        account.setId(id);
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAccountsBetweenAmount(BigDecimal from, BigDecimal to) {
        return accountRepository.getAccountsBetween(from, to);
    }

    @Override
    public Optional<Account> getAccountByUserIdAndCurrency(Long userId, String code) {
        return accountRepository.findAccountByUserIdAndCurrencyCode(userId, code);
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        Account account = accountRepository.findAccountById(id);
        account.setBalance(account.getBalance().add(amount));
        updateAccount(id, account);
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        Account account = accountRepository.findAccountById(id);
        account.setBalance(account.getBalance().subtract(amount));
        updateAccount(id, account);
    }
}
