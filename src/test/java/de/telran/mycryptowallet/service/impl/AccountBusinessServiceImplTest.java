package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountBusinessServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountValidator accountValidator;
    @Mock
    private AccountService accountService;
    @Mock
    private CurrencyService currencyService;
    @Mock
    private UserService userService;
    @Mock
    private RateService rateService;
    @Mock
    private AccountManagerService accountManagerService;
    private final int SCALE = 2;
    @InjectMocks
    private AccountBusinessServiceImpl accountBusinessService;

    @Test
    void deposit() {
        BigDecimal amount = new BigDecimal("100");
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("500"));
        Currency currency = new Currency();
        currency.setCode("USD");
        account.setCurrency(currency);

        accountBusinessService.deposit(account, amount);

        verify(accountService).updateAccount(eq(account.getId()), argThat(updatedAccount ->
                updatedAccount.getBalance().compareTo(new BigDecimal("600")) == 0));
        verify(accountManagerService).depositManager(eq(currency.getCode()), eq(amount));
    }

    @Test
    void withdraw() {
        BigDecimal amount = new BigDecimal("100");
        Account account = new Account();
        account.setId(2L);
        account.setBalance(new BigDecimal("500"));
        Currency currency = new Currency();
        currency.setCode("USD");
        account.setCurrency(currency);


        accountBusinessService.withdraw(account, amount);

        verify(accountService).updateAccount(eq(account.getId()), argThat(updatedAccount ->
                updatedAccount.getBalance().compareTo(new BigDecimal("400")) == 0));
        verify(accountManagerService).withdrawManager(eq(currency.getCode()), eq(amount));
    }

    @Test
    void reserveForOrder() {
        User user = new User(1L, "testUser", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        String currencyCode = "USDT";
        OperationType operationType = OperationType.BUY;
        BigDecimal amount = new BigDecimal("10");
        BigDecimal rate = new BigDecimal("50000");

        Account account = new Account(1L, "address", user, currencyService.getCurrencyByCode(currencyCode), new BigDecimal("100000"), BigDecimal.ZERO);

        when(currencyService.getBasicCurrency()).thenReturn("USDT");
        when(accountRepository.findAccountByUserIdAndCurrencyCode(user.getId(), currencyCode)).thenReturn(account);

        accountBusinessService.reserveForOrder(user, currencyCode, operationType, amount, rate);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountService).updateAccount(eq(account.getId()), accountCaptor.capture());

        Account updatedAccount = accountCaptor.getValue();
        assertNotNull(updatedAccount);
    }

    @Test
    void returnPartOrder() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000"));
        account.setOrderBalance(new BigDecimal("200"));

        BigDecimal returnAmount = new BigDecimal("100");

        accountBusinessService.returnPartOrder(account, returnAmount);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);

        verify(accountService).updateAccount(eq(account.getId()), accountCaptor.capture());

        Account updatedAccount = accountCaptor.getValue();

        assertEquals(new BigDecimal("900"), updatedAccount.getBalance(), "Баланс аккаунта должен быть уменьшен на величину возвращаемой суммы.");
        assertEquals(returnAmount, updatedAccount.getOrderBalance(), "Ордер баланс должен быть равен возвращаемой сумме.");
    }

}