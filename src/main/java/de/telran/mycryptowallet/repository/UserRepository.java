package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 18.01.2024.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    List<User> findUsersByUserStatus(UserStatus userStatus);

    Boolean existsUserByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM crypto_users")
    List<User> getAllUsers();

}
