package de.telran.mycryptowallet.dto;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderAddDTOTest {

    @Test
    void testGettersAndSetters() {
        OrderAddDTO dto = new OrderAddDTO();
        String expectedCurrencyCode = "BTC";
        OperationType expectedOperationType = OperationType.BUY;
        BigDecimal expectedAmount = new BigDecimal("100.00");
        BigDecimal expectedOrderRate = new BigDecimal("50000.00");

        dto.setCurrencyCode(expectedCurrencyCode);
        dto.setOperationType(expectedOperationType);
        dto.setAmount(expectedAmount);
        dto.setOrderRate(expectedOrderRate);

        assertEquals(expectedCurrencyCode, dto.getCurrencyCode());
        assertEquals(expectedOperationType, dto.getOperationType());
        assertEquals(expectedAmount, dto.getAmount());
        assertEquals(expectedOrderRate, dto.getOrderRate());
    }

    @Test
    void testEqualsAndHashCode() {
        OrderAddDTO dto1 = new OrderAddDTO();
        dto1.setCurrencyCode("BTC");
        dto1.setOperationType(OperationType.BUY);
        dto1.setAmount(new BigDecimal("100"));
        dto1.setOrderRate(new BigDecimal("50000"));

        OrderAddDTO dto2 = new OrderAddDTO();
        dto2.setCurrencyCode("BTC");
        dto2.setOperationType(OperationType.BUY);
        dto2.setAmount(new BigDecimal("100"));
        dto2.setOrderRate(new BigDecimal("50000"));

        OrderAddDTO dto3 = new OrderAddDTO();
        dto3.setCurrencyCode("BTC");
        dto3.setOperationType(OperationType.SELL);
        dto3.setAmount(new BigDecimal("50"));
        dto3.setOrderRate(new BigDecimal("1500"));

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);

        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        OrderAddDTO dto = new OrderAddDTO();
        dto.setCurrencyCode("BTC");
        dto.setOperationType(OperationType.BUY);
        dto.setAmount(new BigDecimal("100"));
        dto.setOrderRate(new BigDecimal("50000"));

        String toStringResult = dto.toString();
        assertTrue(toStringResult.contains("BTC"));
        assertTrue(toStringResult.contains("100"));
        assertTrue(toStringResult.contains("50000"));
        assertTrue(toStringResult.contains(OperationType.BUY.name()));
    }

}