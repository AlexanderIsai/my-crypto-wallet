package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.AccountAddDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.exceptions.ExistAccountException;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.interfaces.UserService;
import de.telran.mycryptowallet.service.utils.PublicAddressGenerator;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import de.telran.mycryptowallet.service.utils.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private ActiveUserService activeUserService;

    @Mock
    private AccountValidator accountValidator;

    @Mock
    private PublicAddressGenerator publicAddressGenerator;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User activeUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        activeUser = new User();
        activeUser.setId(1L);
        // Дополнительная настройка activeUser
    }

    @Test
    void addNewAccount_createsAndSavesAccount() {
        String currencyCode = "USD";
        Currency currency = new Currency(currencyCode, "US Dollar");
        String generatedAddress = "generatedAddress";

        when(activeUserService.getActiveUser()).thenReturn(activeUser);
        when(currencyService.getCurrencyByCode(currencyCode)).thenReturn(currency);
        when(publicAddressGenerator.generatePublicAddress(currencyCode)).thenReturn(generatedAddress);

        accountService.addNewAccount(activeUser, currencyCode);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());

        Account savedAccount = accountCaptor.getValue();
        assertNotNull(savedAccount);
        assertEquals(activeUser, savedAccount.getUser());
        assertEquals(currency, savedAccount.getCurrency());
        assertEquals(generatedAddress, savedAccount.getPublicAddress());
        assertEquals(BigDecimal.ZERO, savedAccount.getBalance());
        assertEquals(BigDecimal.ZERO, savedAccount.getOrderBalance());
    }
}