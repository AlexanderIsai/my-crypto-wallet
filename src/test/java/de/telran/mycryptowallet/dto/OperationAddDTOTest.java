package de.telran.mycryptowallet.dto;
import de.telran.mycryptowallet.dto.operationDTO.OperationAddDTO;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OperationAddDTOTest {

    @Test
    void testGettersAndSetters() {
        OperationAddDTO dto = new OperationAddDTO();
        String expectedCurrencyCode = "BTC";
        BigDecimal expectedAmount = new BigDecimal("100.00");
        OperationType expectedType = OperationType.BUY;

        dto.setCurrencyCode(expectedCurrencyCode);
        dto.setAmount(expectedAmount);
        dto.setType(expectedType);

        assertEquals(expectedCurrencyCode, dto.getCurrencyCode());
        assertEquals(expectedAmount, dto.getAmount());
        assertEquals(expectedType, dto.getType());
    }

    @Test
    void testEqualsAndHashCode() {
        OperationAddDTO dto1 = new OperationAddDTO();
        dto1.setCurrencyCode("BTC");
        dto1.setAmount(new BigDecimal("100"));
        dto1.setType(OperationType.BUY);

        OperationAddDTO dto2 = new OperationAddDTO();
        dto2.setCurrencyCode("BTC");
        dto2.setAmount(new BigDecimal("100"));
        dto2.setType(OperationType.BUY);

        OperationAddDTO dto3 = new OperationAddDTO();
        dto3.setCurrencyCode("BTC");
        dto3.setAmount(new BigDecimal("50"));
        dto3.setType(OperationType.SELL);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);

        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        OperationAddDTO dto = new OperationAddDTO();
        dto.setCurrencyCode("BTC");
        dto.setAmount(new BigDecimal("100"));
        dto.setType(OperationType.BUY);

        String toStringResult = dto.toString();
        assertTrue(toStringResult.contains("BTC"));
        assertTrue(toStringResult.contains("100"));
        assertTrue(toStringResult.contains(OperationType.BUY.name()));
    }

}