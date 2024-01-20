package de.telran.mycryptowallet.service;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;

import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 20.01.2024.
 */
public interface UserService {

    void addNewUser(User user);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    User updateUser(User user);

    List<User> getUsersByStatus(UserStatus status);

    Boolean isExistUserByEmail(String email);

}
