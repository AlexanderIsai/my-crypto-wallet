package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 14.02.2024.
 */
@Service
@RequiredArgsConstructor
public class AccountManagerServiceImpl implements AccountManagerService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final UserService userService;
    private final CurrencyService currencyService;
    private final RateService rateService;

    @Override
    public void depositManager(String code, BigDecimal amount) {
        Account account = getManagerAccountByCurrency(code);
        account.setBalance(account.getBalance().add(amount));
        accountService.updateAccount(account.getId(), account);
    }

    @Override
    public void withdrawManager(String code, BigDecimal amount) {
        Account account = getManagerAccountByCurrency(code);
        account.setBalance(account.getBalance().subtract(amount));
        accountService.updateAccount(account.getId(), account);
    }

    private Account getManagerAccountByCurrency(String code) {
        User manager = userService.getUserByEmail("manager@ukr.net");
        return accountRepository.findAccountByUserIdAndCurrencyCode(manager.getId(), code);
    }
    @Override
    public void buyManager(String code, BigDecimal amount){
        withdrawManager(currencyService.getBasicCurrency(), amount.multiply(rateService.getFreshRate(code).getValue()));
        depositManager(currencyService.getBasicCurrency(), amount.multiply(rateService.getFreshRate(code).getSellRate()));
    }

    @Override
    public void sellManager(String code, BigDecimal amount) {
        withdrawManager(currencyService.getBasicCurrency(), amount.multiply(rateService.getFreshRate(code).getBuyRate()));
        depositManager(currencyService.getBasicCurrency(), amount.multiply(rateService.getFreshRate(code).getValue()));
    }
}
