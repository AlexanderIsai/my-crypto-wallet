package de.telran.mycryptowallet.service.utils.validators;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.exceptions.IncorrectInputException;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
import de.telran.mycryptowallet.exceptions.EntityNotFoundException;
import de.telran.mycryptowallet.repository.UserRepository;
import de.telran.mycryptowallet.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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
            throw new EntityNotFoundException("User is not found!!!");
        }
    }
    public void isEmailPresent(List<User> users, String email){
        boolean isEmailExist = users.stream()
                .filter(Objects::nonNull)
                .map(User::getEmail)
                .toList()
                .contains(email);
        if(isEmailExist){
            throw new IncorrectInputException("This email is already exist!!!");
        }

    }
}
