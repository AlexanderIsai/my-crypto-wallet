package de.telran.mycryptowallet.service.utils.validators;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
import de.telran.mycryptowallet.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author Alexander Isai on 03.02.2024.
 */
@Component
@RequiredArgsConstructor
public class UserValidator {

    public void isUserBlock(UserStatus userStatus) {

        if(userStatus.equals(UserStatus.BLOCKED)){
            throw new UserIsBlockedException("User is blocked!!!");
        }
    }
    public void isUserNotFound(User user) {
        if(user == null){
            throw new UserNotFoundException("User is not found!!!");
        }
    }
}
