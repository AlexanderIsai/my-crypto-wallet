package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
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
    private final String BASIC_ACCOUNT = "USDT";
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

        cashFlow(operationAddDTO.getType(), operationUser.getId(), operationCurrency.getCode(), operationAddDTO.getAmount());

        operationRepository.save(operation);
    }

    @Override
    public void cashFlow(OperationType type, Long userId, String code, BigDecimal amount) {
        switch (type) {
            case DEPOSIT:
                accountService.deposit(accountService.getAccountByUserIdAndCurrency(userId, code).orElseThrow().getId(), amount);
                break;
            case WITHDRAW:
                accountService.withdraw(accountService.getAccountByUserIdAndCurrency(userId, code).orElseThrow().getId(), amount);
                break;
            case BUY:
                buy(userId, code, amount);
                break;
            case SELL:
                sell(userId, code, amount);
        }


    }

    @Override
    public void buy(Long userId, String code, BigDecimal amount) {
        Account accountSell = accountService.getAccountByUserIdAndCurrency(userId, BASIC_ACCOUNT).orElseThrow();
        Account accountBuy = accountService.getAccountByUserIdAndCurrency(userId, code).orElseThrow();
        accountService.withdraw(accountSell.getId(), amount);
        accountService.deposit(accountBuy.getId(), amount);
    }

    @Override
    public void sell(Long userId, String code, BigDecimal amount) {
        Account accountBuy = accountService.getAccountByUserIdAndCurrency(userId, BASIC_ACCOUNT).orElseThrow();
        Account accountSell = accountService.getAccountByUserIdAndCurrency(userId, code).orElseThrow();
        accountService.withdraw(accountSell.getId(), amount);
        accountService.deposit(accountBuy.getId(), amount);
    }
}
