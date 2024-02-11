package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
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
@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final ActiveUserService activeUserService;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final RateService rateService;
    private final AccountValidator accountValidator;
    @Override
    @Transactional
    public void addExchangeOperation(OperationAddDTO operationAddDTO) {
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

    public void addOrderOperation(User orderOwner, User orderExecutor, Order order, BigDecimal amount) {
        Operation operationBuy = new Operation();
        Operation operationSell = new Operation();

        Account accountBuy = accountService.getAccountByUserIdAndCurrency(orderOwner.getId(), order.getCurrency().getCode()).orElseThrow();
        Account accountSell = accountService.getAccountByUserIdAndCurrency(orderExecutor.getId(), order.getCurrency().getCode()).orElseThrow();


        operationBuy.setUser(orderOwner);
        operationSell.setUser(orderExecutor);

        operationBuy.setAccount(accountBuy);
        operationSell.setAccount(accountSell);

        operationBuy.setCurrency(order.getCurrency());
        operationSell.setCurrency(order.getCurrency());

        operationBuy.setRateValue(order.getRateValue());
        operationSell.setRateValue(order.getRateValue());

        operationBuy.setAmount(amount);
        operationSell.setAmount(amount);

        operationBuy.setType(OperationType.BUY);
        operationSell.setType(OperationType.SELL);

        operationRepository.save(operationBuy);
        operationRepository.save(operationSell);

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
                break;
        }
    }

    @Override
    public void buy(Operation operation) throws NotEnoughFundsException {
        Account accountSell = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), currencyService.getBasicCurrency()).orElseThrow();
        Account accountBuy = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), operation.getCurrency().getCode()).orElseThrow();
        accountService.withdraw(accountSell.getId(), operation.getAmount().multiply(operation.getRateValue()));
        accountService.deposit(accountBuy.getId(), operation.getAmount());
    }

    @Override
    public void sell(Operation operation) throws NotEnoughFundsException {
        Account accountBuy = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), currencyService.getBasicCurrency()).orElseThrow();
        Account accountSell = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), operation.getCurrency().getCode()).orElseThrow();
        accountService.withdraw(accountSell.getId(), operation.getAmount());
        accountService.deposit(accountBuy.getId(), operation.getAmount().multiply(operation.getRateValue()));
    }

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
