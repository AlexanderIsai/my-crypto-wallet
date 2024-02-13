package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 13.02.2024.
 */
public interface AccountBusinessService {


    void deposit(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount);
    void reserveForOrder(User user, String code, OperationType type, BigDecimal amount, BigDecimal rate);
    Account getAccountFromOrder(Order order);
    void returnPartOrder(Account account, BigDecimal amount);
    TotalUserBalance getTotalUserBalance(Long userId);
}
