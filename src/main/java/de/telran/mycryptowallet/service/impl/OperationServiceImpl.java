package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.repository.OperationRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    @Override
    public void addOperation(OperationAddDTO operationAddDTO) {
        Operation operation = new Operation();

        User operationUser = activeUserService.getActiveUser();
        operation.setUser(operationUser);

        Currency operationCurrency = currencyService.getCurrencyByCode(operationAddDTO.getCurrencyCode());
        operation.setCurrency(operationCurrency);

        Account operationAccount = accountService.getAccountByUserIdAndCurrency(operationUser.getId(), operationCurrency.getCode()).orElseThrow();
        operation.setAccount(operationAccount);

        Rate operationRate = rateService.getFreshRate(operationAddDTO.getCurrencyCode());
        operation.setRate(operationRate);

        operation.setType(operationAddDTO.getType());

        operationRepository.save(operation);
    }
}
