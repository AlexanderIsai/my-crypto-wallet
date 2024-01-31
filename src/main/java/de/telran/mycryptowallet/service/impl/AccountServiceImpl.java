package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.AccountAddDTO;
import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.repository.CurrencyRepository;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.utils.BalanceValidator;
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

    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;
    private final ActiveUserService activeUserService;

    @Override
    public void addNewAccount(AccountAddDTO accountAddDTO) {
        Account account = new Account();
        account.setUser(activeUserService.getActiveUser());
        account.setPublicAddress(PublicAddressGenerator.generatePublicAddress(accountAddDTO.getCurrencyCode()));
        account.setCurrency(currencyService.getCurrencyByCode(accountAddDTO.getCurrencyCode()));
        account.setBalance(BigDecimal.ZERO);
        account.setOrderBalance(BigDecimal.ZERO);
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
//TODO проверять, а есть ли аккаунт??
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
    public void withdraw(Long id, BigDecimal amount) throws NotEnoughFundsException {
        Account account = accountRepository.findAccountById(id);
        BalanceValidator.isEnoughMoney(account, amount);
        account.setBalance(account.getBalance().subtract(amount));
        updateAccount(id, account);
    }

    @Override
    public void reserveForOrder(OrderAddDTO orderAddDTO) throws NotEnoughFundsException {
        User user = activeUserService.getActiveUser();
        switch (orderAddDTO.getOperationType()){
            case BUY:
                Account accountBuy = accountRepository.findAccountByUserIdAndCurrencyCode(user.getId(), currencyService.getBasicCurrency()).orElseThrow();
                BalanceValidator.isEnoughMoney(accountBuy, orderAddDTO.getAmount().multiply(orderAddDTO.getOrderRate()));
                accountBuy.setOrderBalance(accountBuy.getOrderBalance().add(orderAddDTO.getAmount().multiply(orderAddDTO.getOrderRate())));
                accountBuy.setBalance(accountBuy.getBalance().subtract(orderAddDTO.getAmount().multiply(orderAddDTO.getOrderRate())));
                updateAccount(accountBuy.getId(), accountBuy);
                break;
            case SELL:
                Account accountSell = accountRepository.findAccountByUserIdAndCurrencyCode(user.getId(), orderAddDTO.getCurrencyCode()).orElseThrow();
                BalanceValidator.isEnoughMoney(accountSell, orderAddDTO.getAmount());
                accountSell.setOrderBalance(accountSell.getOrderBalance().add(orderAddDTO.getAmount()));
                accountSell.setBalance(accountSell.getBalance().subtract(orderAddDTO.getAmount()));
                updateAccount(accountSell.getId(), accountSell);
                break;
        }
    }

    @Override
    public Account getAccountFromOrder(Order order) {
        if(order.getType().equals(OperationType.BUY)){
            return accountRepository.findAccountByUserIdAndCurrencyCode(order.getUser().getId(), currencyService.getBasicCurrency()).orElseThrow();
        }
        else {
            return accountRepository.findAccountByUserIdAndCurrencyCode(order.getUser().getId(), order.getCurrency().getCode()).orElseThrow();
        }

    }

    @Override
    public void returnPartOrder(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
        account.setOrderBalance(amount);
        updateAccount(account.getId(), account);
    }
    //TODO проверить, достаточно ли средств




}
