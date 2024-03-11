package de.telran.mycryptowallet.service.interfaces;
import de.telran.mycryptowallet.dto.userDTO.UserAddDTO;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import java.util.List;


/**
 * description
 * The UserService interface provides a set of methods for managing users within the application.
 * It offers functionalities to add, retrieve, update, and manage the status of users.
 * @author Alexander Isai on 20.01.2024.
 */
public interface UserService {
    /**
     * Adds a new user to the system with the provided user name, email, and password.
//     * @param userName the user name for the new user, must be non-null
//     * @param email the email address for the new user, must be unique and follow a valid email format
//     * @param password the password for the new user, must meet the system's security requirements
     */
    void addNewUser(UserAddDTO userAddDTO);

    /**
     * Retrieves a user by their ID.
     *
     * @param id The unique ID of the user.
     * @return The User entity.
     */
    User getUserById(Long id);

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user.
     * @return the User entity.
     */
    User getUserByEmail(String email);

    /**
     * Retrieves all users in the system.
     *
     * @return A List of User entities.
     */
    List<User> getAllUsers();

    /**
     * Updates the information of an existing user.
     *
     * @param id The unique ID of the user to be updated.
     * @param user The User entity with updated information.
     */
    void updateUser(Long id, User user);

    /**
     * Retrieves users by their status.
     *
     * @param status The status of the users to be retrieved.
     * @return A List of User entities with the specified status.
     */
    List<User> getUsersByStatus(UserStatus status);

    Boolean isExistUserByEmail(String email);

    /**
     * Set the block/unblock status of a user.
     *
     * @param id The unique ID of the user whose block status is to be toggled.
     */
    void toggleBlockUser(Long id);

    /**
     * Changes the password of a user.
     *
     * @param id The unique ID of the user whose password is to be changed.
     * @param newPassword The new password for the user.
     */
    void changeUserPassword(Long id, String newPassword);


}
