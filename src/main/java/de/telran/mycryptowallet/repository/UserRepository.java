package de.telran.mycryptowallet.repository;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

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
     * Finds a user by their email.
     *
     * @param email The email of the user to find.
     * @return An {@link User}.
     */
    User findUserByEmail(String email);

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

}