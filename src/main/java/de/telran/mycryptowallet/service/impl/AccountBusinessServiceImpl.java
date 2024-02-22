package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 13.02.2024.
 */
@Service
@RequiredArgsConstructor
public class AccountBusinessServiceImpl implements AccountBusinessService {

    private final AccountRepository accountRepository;
    private final AccountValidator accountValidator;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final UserService userService;
    private final RateService rateService;
    private final AccountManagerService accountManagerService;
    private final static int SCALE = 2;

    @Transactional
    @Override
    public void deposit(Account account, BigDecimal amount) {
        accountValidator.isCorrectNumber(amount);
        account.setBalance(account.getBalance().add(amount));
        accountService.updateAccount(account.getId(), account);
        accountManagerService.depositManager(account.getCurrency().getCode(), amount);
    }

    @Transactional
    @Override
    public void withdraw(Account account, BigDecimal amount) {
        accountValidator.isCorrectNumber(amount);
        accountValidator.isEnoughMoney(account, amount);
        account.setBalance(account.getBalance().subtract(amount));
        accountService.updateAccount(account.getId(), account);
        accountManagerService.withdrawManager(account.getCurrency().getCode(), amount);
    }

    @Transactional
    @Override
    public void reserveForOrder(User user, String code, OperationType type, BigDecimal amount, BigDecimal rate) {
        BigDecimal reserveAmount;
        //accountValidator.isExistUserAccount(getAccountByUserIdAndCurrency(user.getId(), code).orElseThrow());
        switch (type) {
            case BUY:
                Account accountBuy = accountRepository.findAccountByUserIdAndCurrencyCode(user.getId(), currencyService.getBasicCurrency()).orElseThrow();
                reserveAmount = amount.multiply(rate);
                accountValidator.isEnoughMoney(accountBuy, reserveAmount);
                reserveMoney(accountBuy, reserveAmount);
                break;
            case SELL:
                Account accountSell = accountRepository.findAccountByUserIdAndCurrencyCode(user.getId(), code).orElseThrow();
                reserveAmount = amount;
                accountValidator.isEnoughMoney(accountSell, amount);
                reserveMoney(accountSell, reserveAmount);
                break;
        }
    }
    private void reserveMoney(Account account, BigDecimal amount){
        account.setOrderBalance(account.getOrderBalance().add(amount));
        account.setBalance(account.getBalance().subtract(amount));
        accountService.updateAccount(account.getId(), account);
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
        accountService.updateAccount(account.getId(), account);
    }

    @Override
    @Transactional
    public TotalUserBalance getTotalUserBalance(Long userId) {
        //userValidator.isUserNotFound(userService.getUserById(userId));
        List<Account> accounts = accountService.getAccountsByUser(userId);
        TotalUserBalance totalUserBalance = new TotalUserBalance();

        BigDecimal usdFrom = getUsdBalanceFromCryptoAccounts(accounts);
        BigDecimal usdBase = accountService.getAccountByUserIdAndCurrency(userId, currencyService.getBasicCurrency()).orElseThrow().getBalance();
        BigDecimal btcFrom = getBTCBalanceFromCryptoAccounts(accounts);
        BigDecimal btcFromUsd = getBTCBalanceFromUSDAccount(accountService.getAccountByUserIdAndCurrency(userId, currencyService.getBasicCurrency()).orElseThrow());
        BigDecimal btcBase = accountService.getAccountByUserIdAndCurrency(userId, currencyService.getBTCCurrency()).orElseThrow().getBalance();

        totalUserBalance.setUsd(usdBase.add(usdFrom));
        totalUserBalance.setBtc(btcBase.add(btcFrom).add(btcFromUsd));
        totalUserBalance.setUser(userService.getUserById(userId));
        return totalUserBalance;
    }

    @Transactional
    @Override
    public TotalUserBalance getTotalBalance() {
        TotalUserBalance sumOfBalances = new TotalUserBalance();
        List<User> users = userService.getAllUsers();

        BigDecimal usd = users.stream()
                .filter(Objects::nonNull)
                .filter(user -> !user.getRole().equals(UserRole.ROLE_ADMIN))
                .map(user -> getTotalUserBalance(user.getId()))
                .map(TotalUserBalance::getUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal btc = users.stream()
                .filter(Objects::nonNull)
                .filter(user -> !user.getRole().equals(UserRole.ROLE_ADMIN))
                .map(user -> getTotalUserBalance(user.getId()))
                .map(TotalUserBalance::getBtc)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        sumOfBalances.setUsd(usd);
        sumOfBalances.setBtc(btc);
        return sumOfBalances;
    }

    private BigDecimal getUsdBalanceFromCryptoAccounts(List<Account> accounts){
        BigDecimal usdFrom = accounts.stream()
                .filter(Objects::nonNull)
                .filter(account -> !account.getCurrency().getCode().equals(currencyService.getBasicCurrency()))
                .map(account -> account.getBalance().multiply(rateService.getFreshRate(account.getCurrency().getCode()).getBuyRate()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return usdFrom;
    }

    private BigDecimal getBTCBalanceFromCryptoAccounts(List<Account> accounts){
        BigDecimal btcFrom = accounts.stream()
                .filter(Objects::nonNull)
                .filter(account -> !account.getCurrency().getCode().equals(currencyService.getBTCCurrency()) && !account.getCurrency().getCode().equals(currencyService.getBasicCurrency()))
                .map(account -> account.getBalance().multiply(rateService.getFreshRate(account.getCurrency().getCode()).getBuyRate()).divide(rateService.getFreshRate(currencyService.getBTCCurrency()).getSellRate(), SCALE, RoundingMode.HALF_DOWN))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return btcFrom;
    }

    private BigDecimal getBTCBalanceFromUSDAccount(Account account){
        return account.getBalance().divide(rateService.getFreshRate(currencyService.getBTCCurrency()).getSellRate(), SCALE, RoundingMode.HALF_DOWN);
    }

    @Transactional
    @Override
    public TotalUserBalance showProfit() {
        User manager = userService.getUserByEmail("manager@ukr.net").orElseThrow();
        TotalUserBalance profit = new TotalUserBalance();
        BigDecimal usd = getTotalUserBalance(manager.getId()).getUsd().subtract(getTotalBalance().getUsd());
        BigDecimal btc = getTotalUserBalance(manager.getId()).getBtc().subtract(getTotalBalance().getBtc());
        profit.setUsd(usd);
        profit.setBtc(btc);
        return profit;
    }
    //TODO протестить прибыль))) убрать адрес?
}