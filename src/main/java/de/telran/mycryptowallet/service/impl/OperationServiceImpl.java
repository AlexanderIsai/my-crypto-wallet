package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.dto.operationDTO.OperationAddDTO;
import de.telran.mycryptowallet.dto.operationDTO.OperationTransferDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.mapper.operationMapper.OperationMapper;
import de.telran.mycryptowallet.repository.OperationRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final AccountManagerService accountManagerService;
    private final OperationMapper operationMapper;
    private final ManagerUserService managerUserService;
    @Value("${app.transfer.fee}")
    private BigDecimal fee;

    @Override
    @Transactional
    public Operation getExchangeOperation(User user, OperationAddDTO operationAddDTO) {
        Operation operation = operationMapper.toEntity(operationAddDTO);
        accountValidator.isCorrectNumber(operation.getAmount());
        operation.setUser(user);

        Currency operationCurrency = currencyService.getCurrencyByCode(operationAddDTO.getCurrencyCode());
        operation.setCurrency(operationCurrency);

        Account operationAccount = accountService.getAccountByUserIdAndCurrency(user.getId(), operationCurrency.getCode());
        accountValidator.isExistUserAccount(operationAccount);
        operation.setAccount(operationAccount);

        Rate operationRate = rateService.getFreshRate(operationAddDTO.getCurrencyCode());
        operation.setRateValue(rateService.setTransferRate(operationRate, operation.getType()));

        return operation;
    }
    @Transactional
    @Override
    public void addOrderOperation(User orderOwner, User orderExecutor, Order order, BigDecimal amount) {
        Operation buy = getExchangeOperation(orderOwner, new OperationAddDTO(order.getCurrency().getCode(), amount, OperationType.BUY));
        buy.setRateValue(order.getRateValue());
        Operation sell = getExchangeOperation(orderExecutor, new OperationAddDTO(order.getCurrency().getCode(), amount, OperationType.SELL));
        sell.setRateValue(order.getRateValue());
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
        Account accountSell = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), currencyService.getBasicCurrency());
        //Account accountBuy = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), operation.getCurrency().getCode()).orElseThrow();
        Account accountBuy = operation.getAccount();
        accountBusinessService.withdraw(accountSell, operation.getAmount().multiply(operation.getRateValue()));
        accountBusinessService.deposit(accountBuy, operation.getAmount());
        accountManagerService.buyManager(operation.getCurrency().getCode(), operation.getAmount());
    }

    @Override
    public void sell(Operation operation) {
        Account accountBuy = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), currencyService.getBasicCurrency());
        //Account accountSell = accountService.getAccountByUserIdAndCurrency(operation.getUser().getId(), operation.getCurrency().getCode()).orElseThrow();
        Account accountSell = operation.getAccount();
        accountBusinessService.withdraw(accountSell, operation.getAmount());
        accountBusinessService.deposit(accountBuy, operation.getAmount().multiply(operation.getRateValue()));
        accountManagerService.sellManager(operation.getCurrency().getCode(), operation.getAmount());
    }
    //TODO clear comments

    @Override
    public void transfer(Account sender, Account receiver, BigDecimal amount) {
        sender.setBalance(sender.getBalance().subtract(amount));
        accountService.updateAccount(sender.getId(), sender);
        receiver.setBalance(receiver.getBalance().add(amount));
        accountService.updateAccount(receiver.getId(), receiver);
    }

    @Override
    public Operation getOperationById(Long id) {
        return operationRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void transferBetweenUsers(User user, OperationTransferDTO operationTransferDTO) {
        Account sender = accountService.getAccountByUserIdAndCurrency(user.getId(), operationTransferDTO.getCode());
        Account receiver = accountService.getAccountByPublicAddress(operationTransferDTO.getAddress());
        BigDecimal amount = operationTransferDTO.getAmount();
        BigDecimal transferFee = amount.multiply(fee);
        BigDecimal totalAmount = amount.add(transferFee);
        accountValidator.isEnoughMoney(sender, totalAmount);
        accountValidator.isCorrectReceiver(operationTransferDTO.getCode(), receiver);
        sender.setBalance(sender.getBalance().subtract(totalAmount));
        receiver.setBalance(receiver.getBalance().add(amount));
        feeFromTransfer(operationTransferDTO.getCode(), transferFee);
        accountService.updateAccount(sender.getId(), sender);
        accountService.updateAccount(receiver.getId(), receiver);
        operationRepository.save(getExchangeOperation(user, new OperationAddDTO(operationTransferDTO.getCode(), operationTransferDTO.getAmount(), OperationType.SEND)));
        operationRepository.save(getExchangeOperation(receiver.getUser(), new OperationAddDTO(operationTransferDTO.getCode(), operationTransferDTO.getAmount(), OperationType.RECEIVED)));
    }

    private void feeFromTransfer(String code, BigDecimal amount){
        User user = managerUserService.getManager();
        Account account = accountService.getAccountByUserIdAndCurrency(user.getId(), code);
        account.setBalance(account.getBalance().add(amount));
        accountService.updateAccount(account.getId(), account);
    }
}
