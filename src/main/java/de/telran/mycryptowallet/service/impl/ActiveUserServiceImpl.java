package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.utils.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
@Service
@RequiredArgsConstructor
public class ActiveUserServiceImpl implements ActiveUserService {

    private final UserValidator userValidator;

    @Override
    public User getActiveUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userValidator.isUserNotFound(user);
        userValidator.isUserBlock(user.getStatus());
        return user;
    }
}
