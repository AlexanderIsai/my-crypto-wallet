package de.telran.mycryptowallet.dto;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 14.02.2024.
 */
@Data
@AllArgsConstructor
public class OrderTransferDTO {

    private Order order;
    private User owner;
    private User executor;
    private Account ownerBasic;
    private Account ownerOrder;
    private Account executorBasic;
    private Account executorOrder;
    private BigDecimal amount;

}
