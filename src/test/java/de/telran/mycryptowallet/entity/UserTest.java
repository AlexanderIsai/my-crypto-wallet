package de.telran.mycryptowallet.entity;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void userDetailsImplementationTest() {
        User user = new User(
                1L,
                "TestUser",
                "test@telran.de",
                "password",
                UserStatus.ONLINE,
                UserRole.ROLE_USER
        );

        assertEquals("TestUser", user.getUsername());
        assertEquals("test@telran.de", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(UserStatus.ONLINE, user.getStatus());
        assertEquals(UserRole.ROLE_USER, user.getRole());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
    @Test
    void testEquals() {
        User user1 = new User(1L, "user1", "user1@example.com", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        User user2 = new User(1L, "user1", "user1@example.com", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        assertEquals(user1, user2);
    }

    @Test
    void testHashCode() {
        User user = new User(1L, "user", "user@example.com", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        assertEquals(user.hashCode(), user.hashCode());
    }

    @Test
    void testToString() {
        User user = new User(1L, "user", "user@example.com", "password", UserStatus.ONLINE, UserRole.ROLE_USER);
        assertNotNull(user.toString());
    }

    @Test
    void testIsAccountNonExpired() {
        User user = new User();
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        User user = new User();
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        User user = new User();
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        User user = new User();
        assertTrue(user.isEnabled());
    }

}