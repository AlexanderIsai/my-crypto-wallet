package de.telran.mycryptowallet.dto;
import de.telran.mycryptowallet.dto.accountDTO.AccountAddDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountAddDTOTest {
    @Test
    void testGetterAndSetter() {
        AccountAddDTO accountAddDTO = new AccountAddDTO();
        String expectedCurrencyCode = "BTC";

        accountAddDTO.setCurrencyCode(expectedCurrencyCode);

        assertEquals(expectedCurrencyCode, accountAddDTO.getCurrencyCode());
    }

    @Test
    void testEqualsAndHashCode() {
        String currencyCode = "BTC";
        AccountAddDTO accountAddDTO1 = new AccountAddDTO();
        accountAddDTO1.setCurrencyCode(currencyCode);

        AccountAddDTO accountAddDTO2 = new AccountAddDTO();
        accountAddDTO2.setCurrencyCode(currencyCode);

        AccountAddDTO accountAddDTO3 = new AccountAddDTO();
        accountAddDTO3.setCurrencyCode("USDT");

        assertEquals(accountAddDTO1, accountAddDTO2);
        assertNotEquals(accountAddDTO1, accountAddDTO3);

        assertEquals(accountAddDTO1.hashCode(), accountAddDTO2.hashCode());
        assertNotEquals(accountAddDTO1.hashCode(), accountAddDTO3.hashCode());
    }

    @Test
    void testToString() {
        String currencyCode = "BTC";
        AccountAddDTO accountAddDTO = new AccountAddDTO();
        accountAddDTO.setCurrencyCode(currencyCode);

        String toStringResult = accountAddDTO.toString();
        assertTrue(toStringResult.contains(currencyCode));
    }

}