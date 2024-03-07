package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.interfaces.UserService;
import de.telran.mycryptowallet.service.utils.PublicAddressGenerator;
import de.telran.mycryptowallet.service.utils.validators.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CurrencyService currencyService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PublicAddressGenerator publicAddressGenerator;
    @Mock
    private UserService userService;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void addNewAccount() {
        User user = new User();
        String code = "BTC";
        String publicAddress = "publicAddress";
        Currency currency = new Currency();
        currency.setCode(code);
        when(publicAddressGenerator.generatePublicAddress(code)).thenReturn(publicAddress);
        when(currencyService.getCurrencyByCode(code)).thenReturn(currency);

        accountService.addNewAccount(user, code);
        verify(accountRepository).save(argThat(account ->
                account.getUser().equals(user) &&
                        account.getPublicAddress().equals(publicAddress) &&
                        account.getCurrency().equals(currency) &&
                        account.getBalance().compareTo(BigDecimal.ZERO) == 0 &&
                        account.getOrderBalance().compareTo(BigDecimal.ZERO) == 0
        ));

    }

    @Test
    void addAccountsWithNewCurrency() {

        String currencyCode = "BTC";
        List<User> users = List.of(new User());
        when(userService.getAllUsers()).thenReturn(users);

        accountService.addAccountsWithNewCurrency(currencyCode);

        verify(userService).getAllUsers();
    }

//    @Test
//    void getAllAccounts() {
//        List<Account> mockAccounts = Arrays.asList(new Account(), new Account(), new Account());
//        when(accountRepository.getAllAccounts()).thenReturn(mockAccounts);
//
//        List<Account> result = accountService.getAllAccounts();
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals(3, result.size());
//        verify(accountRepository).getAllAccounts();
//    }

    @Test
    void getAccountsByUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        List<Account> expectedAccounts = List.of(new Account());
        when(accountRepository.findAccountsByUserId(userId)).thenReturn(expectedAccounts);

        List<Account> actualAccounts = accountService.getAccountsByUser(userId);

        assertEquals(expectedAccounts, actualAccounts);
        verify(accountRepository).findAccountsByUserId(userId);
    }

    @Test
    void getAccountsByCurrency() {
        String currencyCode = "USD";
        List<Account> expectedAccounts = List.of(new Account());
        when(accountRepository.findAccountsByCurrencyCode(currencyCode)).thenReturn(expectedAccounts);

        List<Account> actualAccounts = accountService.getAccountsByCurrency(currencyCode);

        assertEquals(expectedAccounts, actualAccounts);
        verify(accountRepository).findAccountsByCurrencyCode(currencyCode);
    }

    @Test
    void getAccountByPublicAddress() {
        String publicAddress = "Address";
        Account expectedAccount = new Account();
        expectedAccount.setPublicAddress(publicAddress);
        when(accountRepository.findAccountByPublicAddress(publicAddress)).thenReturn(expectedAccount);

        Account actualAccount = accountService.getAccountByPublicAddress(publicAddress);

        assertEquals(expectedAccount, actualAccount);
        verify(accountRepository).findAccountByPublicAddress(publicAddress);
    }

    @Test
    void updateAccount() {
        Long accountId = 1L;
        Account accountToUpdate = new Account();
        accountToUpdate.setId(accountId);
        accountToUpdate.setBalance(new BigDecimal("100.00"));

        accountService.updateAccount(accountId, accountToUpdate);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        Account capturedAccount = accountCaptor.getValue();

        assertEquals(accountId, capturedAccount.getId());
        assertEquals(new BigDecimal("100.00"), capturedAccount.getBalance());
    }

    @Test
    void getAccountByUserIdAndCurrency() {
        Long userId = 1L;
        String currencyCode = "BTC";
        Account expectedAccount = new Account();
        when(accountRepository.findAccountByUserIdAndCurrencyCode(userId, currencyCode)).thenReturn(expectedAccount);

        Account actualAccount = accountService.getAccountByUserIdAndCurrency(userId, currencyCode);

        //assertTrue(actualAccount.isPresent());
        assertEquals(expectedAccount, actualAccount);
        verify(accountRepository).findAccountByUserIdAndCurrencyCode(userId, currencyCode);
    }

    @Test
    void createUserAccounts() {
        User user = new User();
        user.setId(1L);
        List<Currency> currencies = List.of(new Currency("BTC", "Bitcoin"), new Currency("ETH", "Ethereum"));
        when(currencyService.getAllCurrencies()).thenReturn(currencies);
        currencies.forEach(currency ->
                when(currencyService.getCurrencyByCode(currency.getCode())).thenReturn(currency)
        );
        accountService.createUserAccounts(user);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(currencies.size())).save(accountCaptor.capture());
        List<Account> savedAccounts = accountCaptor.getAllValues();

        assertEquals(currencies.size(), savedAccounts.size());
        assertTrue(savedAccounts.stream().allMatch(account -> account.getUser().equals(user) && account.getCurrency() != null));
        assertTrue(savedAccounts.stream().anyMatch(account -> account.getCurrency().getCode().equals("BTC")));
        assertTrue(savedAccounts.stream().anyMatch(account -> account.getCurrency().getCode().equals("ETH")));
    }
}