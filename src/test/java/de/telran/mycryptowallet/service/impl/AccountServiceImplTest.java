package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.AccountAddDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.exceptions.ExistAccountException;
import de.telran.mycryptowallet.repository.AccountRepository;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.utils.PublicAddressGenerator;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


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

    private User user;
    private Account account;
    private AccountAddDTO accountAddDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        account = new Account();
        account.setId(1L);
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);

        accountAddDTO = new AccountAddDTO();
        accountAddDTO.setCurrencyCode("USD");

        when(activeUserService.getActiveUser()).thenReturn(user);
    }

    @Test
    void addNewAccount() {
        when(accountRepository.existsAccountByUserIdAndCurrencyCode(user.getId(), accountAddDTO.getCurrencyCode())).thenReturn(false);
        when(publicAddressGenerator.generatePublicAddress(accountAddDTO.getCurrencyCode())).thenReturn("publicAddress");
        doNothing().when(accountRepository).save(any(Account.class));

        accountService.addNewAccount(accountAddDTO);

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void addNewAccount_ThrowsExistAccountException() {
        when(accountRepository.existsAccountByUserIdAndCurrencyCode(user.getId(), accountAddDTO.getCurrencyCode())).thenReturn(true);

        assertThrows(ExistAccountException.class, () -> accountService.addNewAccount(accountAddDTO));
    }

    @Test
    void getAllAccounts() {
        when(accountRepository.getAllAccounts()).thenReturn(Collections.singletonList(account));

        List<Account> result = accountService.getAllAccounts();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(account, result.get(0));
    }

    @Test
    void getAccountsByUser() {
        Long userId = 1L;
        List<Account> expectedAccounts = List.of(new Account());
        when(accountRepository.findAccountsByUserId(userId)).thenReturn(expectedAccounts);

        List<Account> actualAccounts = accountService.getAccountsByUser(userId);

        assertEquals(expectedAccounts, actualAccounts);
        verify(accountRepository, times(1)).findAccountsByUserId(userId);
    }

    @Test
    void getAccountsByCurrency() {
        String currencyCode = "USD";
        List<Account> expectedAccounts = List.of(new Account());
        when(accountRepository.findAccountsByCurrencyCode(currencyCode)).thenReturn(expectedAccounts);

        List<Account> actualAccounts = accountService.getAccountsByCurrency(currencyCode);

        assertEquals(expectedAccounts, actualAccounts);
        verify(accountRepository, times(1)).findAccountsByCurrencyCode(currencyCode);
    }

    @Test
    void getAccountByPublicAddress() {
        String publicAddress = "testAddress";
        Account expectedAccount = new Account();
        when(accountRepository.findAccountByPublicAddress(publicAddress)).thenReturn(expectedAccount);

        Account actualAccount = accountService.getAccountByPublicAddress(publicAddress);

        assertEquals(expectedAccount, actualAccount);
        verify(accountRepository, times(1)).findAccountByPublicAddress(publicAddress);
    }

    @Test
    void getAccountById() {
        Long accountId = 1L;
        Account expectedAccount = new Account();
        when(accountRepository.findAccountById(accountId)).thenReturn(expectedAccount);

        Account actualAccount = accountService.getAccountById(accountId);

        assertEquals(expectedAccount, actualAccount);
        verify(accountRepository, times(1)).findAccountById(accountId);
    }

    @Test
    void getAccountsByBalanceGreaterThan() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        List<Account> expectedAccounts = Arrays.asList(new Account(), new Account());
        when(accountRepository.findAccountsByBalanceGreaterThan(amount)).thenReturn(expectedAccounts);

        List<Account> actualAccounts = accountService.getAccountsByBalanceGreaterThan(amount);

        assertEquals(expectedAccounts, actualAccounts);
        verify(accountRepository, times(1)).findAccountsByBalanceGreaterThan(amount);
    }
    @Test
    void getAccountsByBalanceLessThan() {
        BigDecimal amount = BigDecimal.valueOf(500);
        List<Account> expectedAccounts = Arrays.asList(new Account(), new Account());
        when(accountRepository.findAccountsByBalanceLessThan(amount)).thenReturn(expectedAccounts);

        List<Account> actualAccounts = accountService.getAccountsByBalanceLessThan(amount);

        assertEquals(expectedAccounts, actualAccounts);
        verify(accountRepository, times(1)).findAccountsByBalanceLessThan(amount);
    }

    @Test
    void existsAccountById() {
        Long accountId = 1L;
        when(accountRepository.existsAccountById(accountId)).thenReturn(true);

        Boolean exists = accountService.existsAccountById(accountId);

        assertTrue(exists);
        verify(accountRepository).existsAccountById(accountId);
    }

    @Test
    void existsAccountByUserIdAndCurrency() {
        Long userId = 1L;
        String currencyCode = "USD";
        when(accountRepository.existsAccountByUserIdAndCurrencyCode(userId, currencyCode)).thenReturn(true);

        Boolean exists = accountService.existsAccountByUserIdAndCurrency(userId, currencyCode);

        assertTrue(exists);
        verify(accountRepository).existsAccountByUserIdAndCurrencyCode(userId, currencyCode);
    }

    @Test
    void updateAccount() {
    }

    @Test
    void getAccountsBetweenAmount() {
    }

    @Test
    void getAccountByUserIdAndCurrency() {
    }

    @Test
    void deposit() {
    }

    @Test
    void withdraw() {
    }

    @Test
    void reserveForOrder() {
    }

    @Test
    void getAccountFromOrder() {
    }

    @Test
    void returnPartOrder() {
    }
}