package de.telran.mycryptowallet.service.utils.validators;

import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
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

    public void isUserBlock(UserStatus userStatus) throws UserIsBlockedException {

        if(userStatus.equals(UserStatus.BLOCKED)){
            throw new UserIsBlockedException("User is blocked!!!");
        }
    }
}
