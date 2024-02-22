package de.telran.mycryptowallet.dto;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTransferDTOTest {
    @Test
    void testAllArgsConstructorAndGetters() {
        User owner = new User();
        User executor = new User();
        Account ownerBasic = new Account();
        Account ownerOrder = new Account();
        Account executorBasic = new Account();
        Account executorOrder = new Account();
        Order order = new Order();
        BigDecimal amount = new BigDecimal("100.00");

        OrderTransferDTO dto = new OrderTransferDTO(order, owner, executor, ownerBasic, ownerOrder, executorBasic, executorOrder, amount);

        assertEquals(order, dto.getOrder());
        assertEquals(owner, dto.getOwner());
        assertEquals(executor, dto.getExecutor());
        assertEquals(ownerBasic, dto.getOwnerBasic());
        assertEquals(ownerOrder, dto.getOwnerOrder());
        assertEquals(executorBasic, dto.getExecutorBasic());
        assertEquals(executorOrder, dto.getExecutorOrder());
        assertEquals(amount, dto.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        Order order1 = new Order();
        User owner1 = new User();
        User executor1 = new User();
        Account ownerBasic1 = new Account();
        Account ownerOrder1 = new Account();
        Account executorBasic1 = new Account();
        Account executorOrder1 = new Account();
        BigDecimal amount1 = new BigDecimal("100.00");

        OrderTransferDTO dto1 = new OrderTransferDTO(order1, owner1, executor1, ownerBasic1, ownerOrder1, executorBasic1, executorOrder1, amount1);
        OrderTransferDTO dto2 = new OrderTransferDTO(order1, owner1, executor1, ownerBasic1, ownerOrder1, executorBasic1, executorOrder1, amount1);
        OrderTransferDTO dtoDifferent = new OrderTransferDTO(new Order(), new User(), new User(), new Account(), new Account(), new Account(), new Account(), new BigDecimal("200.00"));

        assertEquals(dto1, dto2, "DTO с идентичными данными должны быть одинаковыми");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "DTO с идентичными данными должны иметь одинаковый хэш-код");
        assertNotEquals(dto1, dtoDifferent, "DTO с разными данными не должны быть одинаковыми");
    }

    @Test
    void testToString() {
        OrderTransferDTO dto = new OrderTransferDTO(new Order(), new User(), new User(), new Account(), new Account(), new Account(), new Account(), new BigDecimal("100.00"));

        String dtoAsString = dto.toString();
        assertNotNull(dtoAsString, "toString should not return null");
        assertTrue(dtoAsString.contains("100.00"), "toString should include representation of 'amount'");
    }
}