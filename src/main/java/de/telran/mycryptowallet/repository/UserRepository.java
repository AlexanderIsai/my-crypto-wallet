package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Interface for CRUD operations on a repository for the {@link User} type.
 * This interface extends Spring Data JPA's {@link JpaRepository}, providing
 * specialized methods to interact with the User database table.
 * It offers methods to find users by ID, username, or email, check user existence,
 * and retrieve users by their status or fetch all users with custom queries.
 * @author Alexander Isai
 * @version 18.01.2024
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find.
     * @return The found user.
     */
    User findUserById(Long id);

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return The found user.
     */
    User findUserByUserName(String username);

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find.
     * @return An {@link Optional} containing the found user if they exist.
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Finds users by their status.
     *
     * @param userStatus The status of users to find.
     * @return A list of users matching the specified status.
     */
    List<User> findUsersByStatus(UserStatus userStatus);

    /**
     * Checks if a user exists by email.
     *
     * @param email The email to check.
     * @return True if a user with the given email exists, false otherwise.
     */
    Boolean existsUserByEmail(String email);

    /**
     * Checks if a user exists by ID and status.
     *
     * @param id The ID of the user.
     * @param status The status of the user.
     * @return True if a user with the given ID and status exists, false otherwise.
     */
    Boolean existsUserByIdAndStatus(Long id, UserStatus status);

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM crypto_users")
    List<User> getAllUsers();

}