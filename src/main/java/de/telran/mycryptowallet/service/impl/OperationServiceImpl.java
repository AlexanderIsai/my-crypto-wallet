package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.repository.OperationRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
/**
 * description
 *
 * @author Alexander Isai on 24.01.2024.
 */
@RequiredArgsConstructor
@Service
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final RateService rateService;
    private final AccountValidator accountValidator;
    private final AccountBusinessService accountBusinessService;

    @Override
    @Transactional
    public Operation getExchangeOperation(User user, String code, BigDecimal amount, OperationType type) {
        accountValidator.isCorrectNumber(amount);
        Operation operation = new Operation();
        operation.setUser(user);

        Currency operationCurrency = currencyService.getCurrencyByCode(code);
        operation.setCurrency(operationCurrency);

        Account operationAccount = accountService.getAccountByUserIdAndCurrency(user.getId(), operationCurrency.getCode()).orElseThrow();
        accountValidator.isExistUserAccount(operationAccount);
        operation.setAccount(operationAccount);

        Rate operationRate = rateService.getFreshRate(code);
        operation.setRateValue(operationRate.getValue());

        operation.setAmount(amount);

        operation.setType(type);
        return operation;
    }
    @Transactional
    @Override
    public void addOrderOperation(User orderOwner, User orderExecutor, Order order, BigDecimal amount) {
        Operation buy = getExchangeOperation(orderOwner, order.getCurrency().getCode(), amount, OperationType.BUY);
        Operation sell = getExchangeOperation(orderExecutor, order.getCurrency().getCode(), amount, OperationType.SELL);
        operationRepository.save(buy);
        operationRepository.save(sell);
    }

        @Override
    public void cashFlow(Operation operation) {
        switch (operation.getType()) {
            case DEPOSIT:
                accountBusinessService.deposit(operation.getAccount(), operation.getAmount());
                break;
            case WITHDRAW:
                    accountBusinessService.withdraw(operation.getAccount(), operation.getAmount());
                break;
            case BUY:
                buy(operation);
                break;
            case SELL:
                sell(operation);
                break;
        }
        operationRepository.save(operation);
    }

    @Override
    public void buy(Operation operation) {
        Account accountSell = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), currencyService.getBasicCurrency()).orElseThrow();
        //Account accountBuy = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), operation.getCurrency().getCode()).orElseThrow();
        Account accountBuy = operation.getAccount();
        accountBusinessService.withdraw(accountSell, operation.getAmount().multiply(operation.getRateValue()));
        accountBusinessService.deposit(accountBuy, operation.getAmount());
    }

    @Override
    public void sell(Operation operation) {
        Account accountBuy = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), currencyService.getBasicCurrency()).orElseThrow();
        //Account accountSell = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), operation.getCurrency().getCode()).orElseThrow();
        Account accountSell = operation.getAccount();
        accountBusinessService.withdraw(accountSell, operation.getAmount());
        accountBusinessService.deposit(accountBuy, operation.getAmount().multiply(operation.getRateValue()));
    }
    //TODO clear comments

    @Override
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account sender = accountService.getAccountById(fromId);
        Account receiver = accountService.getAccountById(toId);
        sender.setBalance(sender.getBalance().subtract(amount));
        accountService.updateAccount(sender.getId(), sender);
        receiver.setBalance(receiver.getBalance().add(amount));
        accountService.updateAccount(receiver.getId(), receiver);
    }
}
