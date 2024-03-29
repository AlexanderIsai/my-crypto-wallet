package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.PublicAddressGenerator;
import de.telran.mycryptowallet.service.utils.validators.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;


@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;
    private final UserValidator userValidator;
    private final PublicAddressGenerator publicAddressGenerator;
    private final UserService userService;
    @Override
    public void addNewAccount(User user, String code) {
            Account account = new Account();
            account.setUser(user);
            account.setPublicAddress(publicAddressGenerator.generatePublicAddress(code));
            account.setCurrency(currencyService.getCurrencyByCode(code));
            account.setBalance(BigDecimal.ZERO);
            account.setOrderBalance(BigDecimal.ZERO);
            accountRepository.save(account);
    }

    @Transactional
    @Override
    public void addAccountsWithNewCurrency(String code){
        List<User> users = userService.getAllUsers();
        users.forEach(user -> addNewAccount(user, code));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getAccountsByUser(Long userId) {
        userValidator.isUserNotFound(userService.getUserById(userId));
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
        return accountRepository.findById(id).orElseThrow();
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
    @Transactional
    public void resetAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        accounts.forEach(account -> {
            account.setBalance(BigDecimal.ZERO);
            account.setOrderBalance(BigDecimal.ZERO);
            updateAccount(account.getId(), account);
        });
    }

    @Override
    public Account getAccountByUserIdAndCurrency(Long userId, String code) {
        userValidator.isUserNotFound(userService.getUserById(userId));
        return accountRepository.findAccountByUserIdAndCurrencyCode(userId, code);
    }

    @Transactional
    @Override
    public void createUserAccounts(User user) {
        userValidator.isUserNotFound(user);
        currencyService.getAllCurrencies().forEach(currency ->
                addNewAccount(user, currency.getCode())
        );
    }
}
