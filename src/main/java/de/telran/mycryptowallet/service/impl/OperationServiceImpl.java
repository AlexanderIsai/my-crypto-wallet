package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.repository.OperationRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 24.01.2024.
 */
@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final ActiveUserService activeUserService;
    private final AccountService accountService;
    private  final CurrencyService currencyService;
    private final RateService rateService;
    private final String BASIC_CURRENCY = "USDT";
    @Override
    @Transactional
    public void addOperation(OperationAddDTO operationAddDTO) {
        Operation operation = new Operation();

        User operationUser = activeUserService.getActiveUser();
        operation.setUser(operationUser);

        Currency operationCurrency = currencyService.getCurrencyByCode(operationAddDTO.getCurrencyCode());
        operation.setCurrency(operationCurrency);

        Account operationAccount = accountService.getAccountByUserIdAndCurrency(operationUser.getId(), operationCurrency.getCode()).orElseThrow();
        operation.setAccount(operationAccount);

        Rate operationRate = rateService.getFreshRate(operationAddDTO.getCurrencyCode());
        operation.setRateValue(operationRate.getValue());

        operation.setAmount(operationAddDTO.getAmount());

        operation.setType(operationAddDTO.getType());

        cashFlow(operation);

        operationRepository.save(operation);
    }

    @Override
    public void cashFlow(Operation operation) {
        switch (operation.getType()) {
            case DEPOSIT:
                accountService.deposit(operation.getAccount().getId(), operation.getAmount());
                break;
            case WITHDRAW:
                accountService.withdraw(operation.getAccount().getId(), operation.getAmount());
                break;
            case BUY:
                buy(operation);
                break;
            case SELL:
                sell(operation);
        }


    }

    @Override
    public void buy(Operation operation) {
        Account accountSell = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), BASIC_CURRENCY).orElseThrow();
        Account accountBuy = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), operation.getCurrency().getCode()).orElseThrow();
        accountService.withdraw(accountSell.getId(), operation.getAmount().multiply(operation.getRateValue()));
        accountService.deposit(accountBuy.getId(), operation.getAmount());
    }

    @Override
    public void sell(Operation operation) {
        Account accountBuy = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), BASIC_CURRENCY).orElseThrow();
        Account accountSell = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), operation.getCurrency().getCode()).orElseThrow();
        accountService.withdraw(accountSell.getId(), operation.getAmount());
        accountService.deposit(accountBuy.getId(), operation.getAmount().multiply(operation.getRateValue()));
    }
}
