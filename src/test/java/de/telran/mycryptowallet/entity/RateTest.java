package de.telran.mycryptowallet.entity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RateTest {

    @Test
    void testEquals() {
        Currency currency = new Currency("BTC", "Bitcoin");
        Rate rate1 = new Rate(1L, new BigDecimal("50000"), new BigDecimal("50500"), new BigDecimal("49500"), currency, Instant.now());
        Rate rate2 = new Rate(1L, new BigDecimal("50000"), new BigDecimal("50500"), new BigDecimal("49500"), currency, Instant.now());
        Rate rate3 = new Rate(2L, new BigDecimal("60000"), new BigDecimal("60500"), new BigDecimal("59500"), currency, Instant.now());

        assertEquals(rate1, rate2);
        assertNotEquals(rate1, rate3);
    }

    @Test
    void testHashCode() {
        Currency currency = new Currency("BTC", "Bitcoin");
        Rate rate = new Rate(1L, new BigDecimal("50000"), new BigDecimal("50500"), new BigDecimal("49500"), currency, Instant.now());

        assertEquals(rate.hashCode(), rate.hashCode());
    }

    @Test
    void testConstructor() {
        Currency currency = new Currency("BTC", "Bitcoin");
        BigDecimal value = new BigDecimal("50000");
        BigDecimal buyRate = new BigDecimal("50500");
        BigDecimal sellRate = new BigDecimal("49500");
        Instant createdOn = Instant.now();
        Rate rate = new Rate(1L, value, buyRate, sellRate, currency, createdOn);

        assertNotNull(rate);
        assertEquals(1L, rate.getId());
        assertEquals(value, rate.getValue());
        assertEquals(buyRate, rate.getBuyRate());
        assertEquals(sellRate, rate.getSellRate());
        assertEquals(currency, rate.getCurrency());
        assertNotNull(rate.getCreatedOn());
    }

    @Test
    void testToString() {
        Currency currency = new Currency("BTC", "Bitcoin");
        Rate rate = new Rate(1L, new BigDecimal("50000"), new BigDecimal("50500"), new BigDecimal("49500"), currency, Instant.now());

        assertNotNull(rate.toString());
        assertTrue(rate.toString().contains("50000"));
        assertTrue(rate.toString().contains("BTC"));
    }

}