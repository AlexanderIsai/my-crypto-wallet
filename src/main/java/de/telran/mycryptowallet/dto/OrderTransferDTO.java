package de.telran.mycryptowallet.dto;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * The DTO provides a structured way to handle order transactions, linking users and their accounts with specific orders,
 * facilitating the processing of buy/sell operations within the system.
 *
 * @author Alexander Isai
 * @version 14.02.2024
 */
@Data
@AllArgsConstructor
public class OrderTransferDTO {

    /**
     * The order associated with the transfer.
     */
    private Order order;

    /**
     * The user who owns the order.
     */
    private User owner;

    /**
     * The user executing the order.
     */
    private User executor;

    /**
     * The basic account of the order owner.
     */
    private Account ownerBasic;

    /**
     * The specific account of the order owner related to the order.
     */
    private Account ownerOrder;

    /**
     * The basic account of the order executor.
     */
    private Account executorBasic;

    /**
     * The specific account of the order executor related to the order.
     */
    private Account executorOrder;

    /**
     * The amount involved in the order transaction.
     */
    private BigDecimal amount;

}