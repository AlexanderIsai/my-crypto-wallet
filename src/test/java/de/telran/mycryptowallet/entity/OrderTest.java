package de.telran.mycryptowallet.entity;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testEquals() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Currency currency = new Currency("BTC", "Bitcoin");
        BigDecimal rateValue = new BigDecimal("50000");
        BigDecimal amount = new BigDecimal("1");
        Order order1 = new Order(1L, user, currency, rateValue, amount, OperationType.BUY, OrderStatus.ACTIVE);
        Order order2 = new Order(1L, user, currency, rateValue, amount, OperationType.BUY, OrderStatus.ACTIVE);
        Order order3 = new Order(2L, user, currency, rateValue, amount, OperationType.SELL, OrderStatus.DONE);

        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
    }

    @Test
    void testHashCode() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Currency currency = new Currency("BTC", "Bitcoin");
        Order order = new Order(1L, user, currency, new BigDecimal("50000"), new BigDecimal("1"), OperationType.BUY, OrderStatus.ACTIVE);

        assertEquals(order.hashCode(), order.hashCode());
    }

    @Test
    void testConstructor() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Currency currency = new Currency("BTC", "Bitcoin");
        BigDecimal rateValue = new BigDecimal("50000");
        BigDecimal amount = new BigDecimal("1");
        Order order = new Order(1L, user, currency, rateValue, amount, OperationType.BUY, OrderStatus.ACTIVE);

        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(user, order.getUser());
        assertEquals(currency, order.getCurrency());
        assertEquals(rateValue, order.getRateValue());
        assertEquals(amount, order.getAmount());
        assertEquals(OperationType.BUY, order.getType());
        assertEquals(OrderStatus.ACTIVE, order.getStatus());
    }

    @Test
    void testToString() {
        User user = new User(1L, "user", "test@telran.de", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        Currency currency = new Currency("BTC", "Bitcoin");
        Order order = new Order(1L, user, currency, new BigDecimal("50000"), new BigDecimal("1"), OperationType.BUY, OrderStatus.ACTIVE);

        assertNotNull(order.toString());
        assertTrue(order.toString().contains("50000"));
        assertTrue(order.toString().contains("BTC"));
        assertTrue(order.toString().contains("BUY"));
    }

}