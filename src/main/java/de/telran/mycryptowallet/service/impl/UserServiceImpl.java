package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.repository.UserRepository;
import de.telran.mycryptowallet.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 20.01.2024.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void addNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void updateUser(Long id, User updatedUser) {
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    @Override
    public List<User> getUsersByStatus(UserStatus status) {
        return userRepository.findUsersByStatus(status);
    }

    @Override
    public Boolean isExistUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public void toggleBlockUser(Long id) {
        User user = userRepository.findUserById(id);
        if(!user.getStatus().equals(UserStatus.BLOCKED)){
            user.setStatus(UserStatus.BLOCKED);
        }
        else {
            user.setStatus(UserStatus.OFFLINE);
        }
        updateUser(user.getId(), user);
    }

    @Override
    public void changeUserPassword(Long id, String newPassword) {
        User user = userRepository.findUserById(id);
        System.out.println(user);
        user.setPassword(passwordEncoder.encode(newPassword));
        updateUser(id, user);
    }
}
