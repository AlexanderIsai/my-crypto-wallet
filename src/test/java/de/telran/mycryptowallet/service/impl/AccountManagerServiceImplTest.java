package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Rate;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.interfaces.RateService;
import de.telran.mycryptowallet.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountManagerServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private RateService rateService;

    @InjectMocks
    private AccountManagerServiceImpl accountManagerService;

    private final String currencyCode = "BTC";
    private final BigDecimal amount = BigDecimal.valueOf(100);
    private User manager;
    private Account managerAccount;

    @BeforeEach
    void setUp() {
        manager = new User();
        manager.setEmail("manager@ukr.net");
        managerAccount = new Account();
        managerAccount.setId(1L);
        managerAccount.setUser(manager);
        managerAccount.setCurrency(currencyService.getCurrencyByCode(currencyCode));
        managerAccount.setBalance(BigDecimal.ZERO);
        managerAccount.setOrderBalance(BigDecimal.ZERO);

        when(userService.getUserByEmail("manager@ukr.net")).thenReturn(Optional.of(manager));
        when(accountRepository.findAccountByUserIdAndCurrencyCode(manager.getId(), currencyCode)).thenReturn(Optional.of(managerAccount));
    }

    @Test
    void depositManagerTest() {
        accountManagerService.depositManager(currencyCode, amount);

        verify(accountService).updateAccount(eq(managerAccount.getId()), any(Account.class));
    }

    @Test
    void withdrawManagerTest() {
        accountManagerService.withdrawManager(currencyCode, amount);

        verify(accountService).updateAccount(eq(managerAccount.getId()), any(Account.class));
    }
}
