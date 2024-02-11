package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.dto.AccountAddDTO;
import de.telran.mycryptowallet.dto.OrderAddDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.exceptions.ExistAccountException;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import de.telran.mycryptowallet.service.utils.PublicAddressGenerator;
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
 * @author Alexander Isai on 22.01.2024.
 */
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;
    private final ActiveUserService activeUserService;
    private final AccountValidator accountValidator;
    private final PublicAddressGenerator publicAddressGenerator;
    private final RateService rateService;
    private final UserService userService;
    private final int SCALE = 2;

    @Override
    public void addNewAccount(AccountAddDTO accountAddDTO) {
            if(existsAccountByUserIdAndCurrency(activeUserService.getActiveUser().getId(), accountAddDTO.getCurrencyCode())){
                throw new ExistAccountException("Account in this currency already exists for the user");
            }
            Account account = new Account();
            account.setUser(activeUserService.getActiveUser());
            account.setPublicAddress(publicAddressGenerator.generatePublicAddress(accountAddDTO.getCurrencyCode()));
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
        accountValidator.isEnoughMoney(account, amount);
        account.setBalance(account.getBalance().subtract(amount));
        updateAccount(id, account);
    }

    @Override
    public void reserveForOrder(OrderAddDTO orderAddDTO) {
        User user = activeUserService.getActiveUser();
        try {
            if (!existsAccountByUserIdAndCurrency(user.getId(), orderAddDTO.getCurrencyCode())) {
                throw new ExistAccountException("Account does not exist");
            }
            switch (orderAddDTO.getOperationType()) {
                case BUY:
                    Account accountBuy = accountRepository.findAccountByUserIdAndCurrencyCode(user.getId(), currencyService.getBasicCurrency()).orElseThrow();
                    accountValidator.isEnoughMoney(accountBuy, orderAddDTO.getAmount().multiply(orderAddDTO.getOrderRate()));
                        accountBuy.setOrderBalance(accountBuy.getOrderBalance().add(orderAddDTO.getAmount().multiply(orderAddDTO.getOrderRate())));
                        accountBuy.setBalance(accountBuy.getBalance().subtract(orderAddDTO.getAmount().multiply(orderAddDTO.getOrderRate())));
                        updateAccount(accountBuy.getId(), accountBuy);
                    break;
                case SELL:
                    Account accountSell = accountRepository.findAccountByUserIdAndCurrencyCode(user.getId(), orderAddDTO.getCurrencyCode()).orElseThrow();
                    accountValidator.isEnoughMoney(accountSell, orderAddDTO.getAmount());
                    accountSell.setOrderBalance(accountSell.getOrderBalance().add(orderAddDTO.getAmount()));
                    accountSell.setBalance(accountSell.getBalance().subtract(orderAddDTO.getAmount()));
                    updateAccount(accountSell.getId(), accountSell);
                    break;
            }
        }
        catch (ExistAccountException exception){
            throw new RuntimeException(exception.getMessage(), exception);
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

    @Override
    @Transactional
    public TotalUserBalance getTotalUserBalance(Long userId) {
        List<Account> accounts = getAccountsByUser(userId);
        TotalUserBalance totalUserBalance = new TotalUserBalance();
        BigDecimal usdFrom = accounts.stream()
                .filter(Objects::nonNull)
                .filter(account -> !account.getCurrency().getCode().equals(currencyService.getBasicCurrency()))
                .map(account -> account.getBalance().multiply(rateService.getFreshRate(account.getCurrency().getCode()).getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal usdBase = accounts.stream()
                .filter(Objects::nonNull)
                .filter(account -> account.getCurrency().getCode().equals(currencyService.getBasicCurrency()))
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal btcFrom = accounts.stream()
                .filter(Objects::nonNull)
                .filter(account -> !account.getCurrency().getCode().equals(currencyService.getBTCCurrency()) && !account.getCurrency().getCode().equals(currencyService.getBasicCurrency()))
                .map(account -> account.getBalance().multiply(rateService.getFreshRate(account.getCurrency().getCode()).getValue()).divide(rateService.getFreshRate(currencyService.getBTCCurrency()).getValue(), SCALE, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal btcFromUSD = accounts.stream()
                .filter(Objects::nonNull)
                .filter(account -> account.getCurrency().getCode().equals(currencyService.getBasicCurrency()))
                .map(account -> account.getBalance().divide(rateService.getFreshRate(currencyService.getBTCCurrency()).getValue(), SCALE, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal btcBase = accounts.stream()
                .filter(Objects::nonNull)
                .filter(account -> account.getCurrency().getCode().equals(currencyService.getBTCCurrency()))
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalUserBalance.setUsd(usdBase.add(usdFrom));
        totalUserBalance.setBtc(btcBase.add(btcFrom).add(btcFromUSD));
        totalUserBalance.setUser(userService.getUserById(userId));


        return totalUserBalance;
    }

}
