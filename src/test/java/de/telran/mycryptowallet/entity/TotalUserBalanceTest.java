package de.telran.mycryptowallet.entity;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TotalUserBalanceTest {

    @Test
    void testEquals() {
        User user1 = new User(1L, "user1", "email1@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        BigDecimal usdBalance = new BigDecimal("100");
        BigDecimal btcBalance = new BigDecimal("0.5");
        Instant createdOn = Instant.now();

        TotalUserBalance balance1 = new TotalUserBalance(1L, user1, usdBalance, btcBalance, createdOn);
        TotalUserBalance balance2 = new TotalUserBalance(1L, user1, usdBalance, btcBalance, createdOn);
        TotalUserBalance balance3 = new TotalUserBalance(2L, user1, usdBalance, btcBalance, createdOn);

        assertEquals(balance1, balance2);
        assertNotEquals(balance1, balance3);
    }

    @Test
    void testHashCode() {
        User user = new User(1L, "user", "email@example.com", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        TotalUserBalance balance = new TotalUserBalance(1L, user, BigDecimal.valueOf(100), BigDecimal.valueOf(1), Instant.now());
        assertEquals(balance.hashCode(), balance.hashCode());
    }

    @Test
    void testConstructor() {
        User user = new User(1L, "user", "email@example.com", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        BigDecimal usdBalance = new BigDecimal("100");
        BigDecimal btcBalance = new BigDecimal("0.5");
        Instant createdOn = Instant.now();
        TotalUserBalance balance = new TotalUserBalance(1L, user, usdBalance, btcBalance, createdOn);

        assertNotNull(balance);
        assertEquals(1L, balance.getId());
        assertEquals(user, balance.getUser());
        assertEquals(usdBalance, balance.getUsd());
        assertEquals(btcBalance, balance.getBtc());
        assertEquals(createdOn, balance.getCreatedOn());
    }

    @Test
    void testToString() {
        User user = new User(1L, "user", "email@example.com", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        TotalUserBalance balance = new TotalUserBalance(1L, user, BigDecimal.valueOf(100), BigDecimal.valueOf(1), Instant.now());
        assertNotNull(balance.toString());
        assertTrue(balance.toString().contains(user.getUsername()));
        assertTrue(balance.toString().contains("100"));
        assertTrue(balance.toString().contains("1"));
    }

}