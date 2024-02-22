package de.telran.mycryptowallet.entity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void testEquals() {
        Currency currency1 = new Currency("BTC", "Bitcoin");
        Currency currency2 = new Currency("BTC", "Bitcoin");
        Currency currency3 = new Currency("ETH", "Ethereum");
        assertEquals(currency1, currency2);
        assertNotEquals(currency1, currency3);
    }

    @Test
    void testHashCode() {
        Currency currency = new Currency("BTC", "Bitcoin");
        assertEquals(currency.hashCode(), currency.hashCode());
    }

    @Test
    void testToString() {
        Currency currency = new Currency("BTC", "Bitcoin");
        assertNotNull(currency.toString());
        assertTrue(currency.toString().contains("BTC"));
        assertTrue(currency.toString().contains("Bitcoin"));
    }

    @Test
    void testSetTitleAndGetTitle() {
        Currency currency = new Currency();
        String title = "Ethereum";
        currency.setTitle(title);
        assertEquals(title, currency.getTitle());
    }

}