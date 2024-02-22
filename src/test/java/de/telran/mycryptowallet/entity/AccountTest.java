package de.telran.mycryptowallet.entity;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testEquals() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Currency currency = new Currency("BTC", "Bitcoin");
        Account account1 = new Account(1L, "abc123", user, currency, new BigDecimal("100.00"), new BigDecimal("10.00"));
        Account account2 = new Account(1L, "abc123", user, currency, new BigDecimal("100.00"), new BigDecimal("10.00"));
        Account account3 = new Account(2L, "xyz789", user, currency, new BigDecimal("200.00"), new BigDecimal("20.00"));

        assertEquals(account1, account2);
        assertNotEquals(account1, account3);
    }

    @Test
    void testHashCode() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Currency currency = new Currency("BTC", "Bitcoin");
        Account account = new Account(1L, "abc123", user, currency, new BigDecimal("100.00"), new BigDecimal("10.00"));

        assertEquals(account.hashCode(), account.hashCode());
    }

    @Test
    void testConstructor() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Currency currency = new Currency("BTC", "Bitcoin");
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal orderBalance = new BigDecimal("10.00");
        Account account = new Account(1L, "abc123", user, currency, balance, orderBalance);

        assertNotNull(account);
        assertEquals(1L, account.getId());
        assertEquals("abc123", account.getPublicAddress());
        assertEquals(user, account.getUser());
        assertEquals(currency, account.getCurrency());
        assertEquals(balance, account.getBalance());
        assertEquals(orderBalance, account.getOrderBalance());
    }

    @Test
    void testToString() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Currency currency = new Currency("BTC", "Bitcoin");
        Account account = new Account(1L, "abc123", user, currency, new BigDecimal("100.00"), new BigDecimal("10.00"));

        assertNotNull(account.toString());
        assertTrue(account.toString().contains("abc123"));
        assertTrue(account.toString().contains("100.00"));
        assertTrue(account.toString().contains("10.00"));
        assertTrue(account.toString().contains("BTC"));
        assertTrue(account.toString().contains(user.getUsername()));
    }

}