package de.telran.mycryptowallet.service.utils.validators;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.exceptions.ExistAccountException;
import de.telran.mycryptowallet.exceptions.IncorrectInputException;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;
import de.telran.mycryptowallet.repository.AccountRepository;
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
    public void isNotExistUserAccount(Account account){
        System.out.println("IS NOT EXIST USER ACCOUNT");
        if(account != null){
            throw new ExistAccountException("Account in this currency already exists for the user");
        }
    }

    public void isExistUserAccount(Account account){
        if(account == null){
            throw new ExistAccountException("Account is not found");
        }
    }

    public void isCorrectNumber(BigDecimal number){
        if(number.compareTo(BigDecimal.ZERO) < 0){
            throw new IncorrectInputException("You try to input negative number!!!");
        }
    }


}
