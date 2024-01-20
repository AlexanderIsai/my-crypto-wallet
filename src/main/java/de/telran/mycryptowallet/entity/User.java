package de.telran.mycryptowallet.entity;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import jakarta.persistence.*;
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
 * @author Alexander Isai on 16.01.2024.
 * description. The basic essence of the application. Protected by SpringSecurity.
 * It has fields - username, e-mail, password, role, status, account list
 * */
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
//TODO подумать про индексацию и валидацию поля

    @Column(nullable = false, name = "password")
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Account> accounts = new HashSet<>();
    //TODO определиться - нужно ли юзеру это поле

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(userRole.name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
//TODO разобраться с этими четырьмя методами
}
