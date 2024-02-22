package de.telran.mycryptowallet.entity;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Test
    void testEqualsAndHashCode() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Account account = new Account(1L, "publicAddress", user, new Currency("BTC", "Bitcoin"), new BigDecimal("100"), new BigDecimal("10"));
        Currency currency = new Currency("BTC", "Bitcoin");
        BigDecimal amount = new BigDecimal("50");
        BigDecimal rateValue = new BigDecimal("50000");
        OperationType type = OperationType.BUY;
        Instant now = Instant.now();

        Operation operation1 = new Operation(1L, user, account, currency, amount, rateValue, type, now);
        Operation operation2 = new Operation(1L, user, account, currency, amount, rateValue, type, now);
        Operation operation3 = new Operation(2L, user, account, currency, amount, rateValue, OperationType.SELL, now);

        assertTrue(operation1.equals(operation2) && operation2.equals(operation1));
        assertEquals(operation1.hashCode(), operation2.hashCode());

        assertFalse(operation1.equals(operation3));
        assertNotEquals(operation1.hashCode(), operation3.hashCode());
    }

    @Test
    void testConstructorAndFields() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Account account = new Account(1L, "publicAddress", user, new Currency("BTC", "Bitcoin"), new BigDecimal("100"), new BigDecimal("10"));
        Currency currency = new Currency("BTC", "Bitcoin");
        BigDecimal amount = new BigDecimal("50");
        BigDecimal rateValue = new BigDecimal("50000");
        OperationType type = OperationType.BUY;
        Instant creationTime = Instant.now();

        Operation operation = new Operation(1L, user, account, currency, amount, rateValue, type, creationTime);

        assertEquals(1L, operation.getId());
        assertEquals(user, operation.getUser());
        assertEquals(account, operation.getAccount());
        assertEquals(currency, operation.getCurrency());
        assertEquals(amount, operation.getAmount());
        assertEquals(rateValue, operation.getRateValue());
        assertEquals(type, operation.getType());
        assertNotNull(operation.getCreatedOn());
    }

}