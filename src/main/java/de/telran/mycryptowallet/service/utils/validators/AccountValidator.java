package de.telran.mycryptowallet.service.utils.validators;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 01.02.2024.
 */
@Component
@RequiredArgsConstructor
public class AccountValidator {

    public void isEnoughMoney(Account account, BigDecimal amount) {

            if (account.getBalance().compareTo(amount) < 0){
                throw new NotEnoughFundsException("Sorry! You don't have enough money. Should you something borrow");
            }
    }

}
