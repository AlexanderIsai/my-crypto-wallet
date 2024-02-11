package de.telran.mycryptowallet.service.interfaces;

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

    User getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    void updateUser(Long id, User user);

    List<User> getUsersByStatus(UserStatus status);

    Boolean isExistUserByEmail(String email);

    void toggleBlockUser(Long id);

    void changeUserPassword(Long id, String newPassword);

}
