package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
@Service
public class ActiveUserServiceImpl implements ActiveUserService {

    @Override
    public User getActiveUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }
}
