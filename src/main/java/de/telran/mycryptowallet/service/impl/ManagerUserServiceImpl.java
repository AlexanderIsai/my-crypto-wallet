package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.repository.UserRepository;
import de.telran.mycryptowallet.service.interfaces.ManagerUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author Alexander Isai on 07.03.2024.
 */
@Service
@RequiredArgsConstructor
public class ManagerUserServiceImpl implements ManagerUserService {

    private final UserRepository userRepository;
    @Value("${app.manager.email}")
    private String email;

    @Override
    public User getManager() {
        return userRepository.findUserByEmail(email);
    }
}
