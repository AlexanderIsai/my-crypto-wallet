package de.telran.mycryptowallet.service.utils;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 01.02.2024.
 */
public class BalanceValidator {

    public static void isEnoughMoney(Account account, BigDecimal amount) throws NotEnoughFundsException {
        if(account.getBalance().compareTo(amount) <= 0){
            throw new NotEnoughFundsException("Sorry! You don`t have enough money");
        }
    }
}
