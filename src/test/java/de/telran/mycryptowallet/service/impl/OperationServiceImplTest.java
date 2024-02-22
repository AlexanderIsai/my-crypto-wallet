package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.repository.OperationRepository;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.interfaces.RateService;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationServiceImplTest {

    @Mock
    private OperationRepository operationRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private CurrencyService currencyService;
    @Mock
    private RateService rateService;
    @Mock
    private AccountValidator accountValidator;

    @InjectMocks
    private OperationServiceImpl operationService;

    @Test
    void getExchangeOperationTest() {
        User user = new User();
        user.setId(2L);
        String code = "BTC";
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal rateValue = BigDecimal.valueOf(50000);
        OperationType type = OperationType.BUY;
        Long operationId = 1L;

        Operation expectedOperation = new Operation();
        expectedOperation.setId(operationId);
        expectedOperation.setUser(user);
        expectedOperation.setType(type);
        expectedOperation.setAmount(amount);
        expectedOperation.setRateValue(rateValue);
        Currency currency = new Currency("BTC", "bitcoin");
        expectedOperation.setCurrency(currency);

        when(operationService.getOperationById(operationId)).thenReturn(Optional.of(expectedOperation));

        Optional<Operation> actualOperation = operationService.getOperationById(operationId);

        assertNotNull(actualOperation);
        assertTrue(actualOperation.isPresent());
        assertEquals(expectedOperation.getId(), actualOperation.get().getId());
        assertEquals(expectedOperation.getType(), actualOperation.get().getType());
        assertEquals(expectedOperation.getAmount(), actualOperation.get().getAmount());

    }
}