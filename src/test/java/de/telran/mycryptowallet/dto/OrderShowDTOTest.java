package de.telran.mycryptowallet.dto;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderShowDTOTest {

    @Test
    void testGettersAndSetters() {
        OrderShowDTO dto = new OrderShowDTO();
        String expectedCurrencyCode = "BTC";
        OperationType expectedOperationType = OperationType.BUY;
        OrderStatus expectedOrderStatus = OrderStatus.ACTIVE;

        dto.setCurrencyCode(expectedCurrencyCode);
        dto.setOperationType(expectedOperationType);
        dto.setOrderStatus(expectedOrderStatus);

        assertEquals(expectedCurrencyCode, dto.getCurrencyCode());
        assertEquals(expectedOperationType, dto.getOperationType());
        assertEquals(expectedOrderStatus, dto.getOrderStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        OrderShowDTO dto1 = new OrderShowDTO("BTC", OperationType.BUY, OrderStatus.ACTIVE);
        OrderShowDTO dto2 = new OrderShowDTO("BTC", OperationType.BUY, OrderStatus.ACTIVE);
        OrderShowDTO dto3 = new OrderShowDTO("ETH", OperationType.SELL, OrderStatus.DONE);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);

        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        OrderShowDTO dto = new OrderShowDTO("BTC", OperationType.BUY, OrderStatus.ACTIVE);

        String toStringResult = dto.toString();
        assertTrue(toStringResult.contains("BTC"));
        assertTrue(toStringResult.contains(OrderStatus.ACTIVE.name()));
        assertTrue(toStringResult.contains(OperationType.BUY.name()));
    }
}