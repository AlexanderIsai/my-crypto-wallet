package de.telran.mycryptowallet.entity;

import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity User
 * @author Alexander Isai
 * @version 1.0
 * @since 16.01.2024
 * Entity User in the cryptocurrency wallet application.
 * It is designed to work with Spring Security for authentication and authorization purposes.
 * This class includes basic user information such as username, email, and password,
 * as well as user role and status for access control.
 */
@Entity
@Table(name = "crypto_users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String userName;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false, name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /**
     *  Retrieves the authorities (permissions) granted to the user.
     *  In this implementation, user's role is converted to a {@link GrantedAuthority}.
     *  @return A collection of the user's granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(this.getRole().name()));
        return authorities;
    }
    /**
     * Retrieves the username of the user.
     *
     * @return The username of the user.
     */
    @Override
    public String getUsername() {
        return userName;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the user's account is valid (non-expired), false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /**
     * Checks if the user's account is locked.
     *
     * @return true if the user's account is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * Indicates whether the user's credentials (password) have expired.
     *
     * @return true if the user's credentials are valid (non-expired), false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /**
     * Checks if the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
//TODO разобраться с этими четырьмя методами
}
