package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.repository.OperationRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
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
    @Mock
    private AccountBusinessService accountBusinessService;
    @Mock
    private AccountManagerService accountManagerService;
    @Mock
    private User user;
    @Mock
    private Currency currency;
    @Mock
    private Account account;
    @Mock
    private Rate rate;

    @InjectMocks
    private OperationServiceImpl operationService;

//    @Test
//    void getExchangeOperationTest() {
//        String code = "BTC";
//        BigDecimal amount = BigDecimal.valueOf(100);
//        OperationType type = OperationType.BUY;
//        BigDecimal sellRate = BigDecimal.valueOf(50000);
//
//        when(user.getId()).thenReturn(1L);
//        when(currency.getCode()).thenReturn(code);
//        when(accountService.getAccountByUserIdAndCurrency(user.getId(), code)).thenReturn(account);
//        when(currencyService.getCurrencyByCode(code)).thenReturn(currency);
//        when(rateService.getFreshRate(code)).thenReturn(rate);
//        when(rate.getSellRate()).thenReturn(sellRate);
//        doNothing().when(accountValidator).isCorrectNumber(amount);
//        doNothing().when(accountValidator).isExistUserAccount(account);
//
//        Operation actualOperation = operationService.getExchangeOperation(user, code, amount, type);
//
//        assertNotNull(actualOperation);
//        assertEquals(user, actualOperation.getUser());
//        assertEquals(currency, actualOperation.getCurrency());
//        assertEquals(account, actualOperation.getAccount());
//        assertEquals(amount, actualOperation.getAmount());
//        assertEquals(type, actualOperation.getType());
//        assertEquals(sellRate, actualOperation.getRateValue());
//    }
}