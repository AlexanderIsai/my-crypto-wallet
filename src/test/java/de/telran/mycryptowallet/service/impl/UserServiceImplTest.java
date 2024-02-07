package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addNewUser() {
        User user = new User();
        user.setPassword("password");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        userService.addNewUser(user);

        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findUserById(userId)).thenReturn(mockUser);

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository).findUserById(userId);
    }

    @Test
    void getUserByEmail() {
        String email = "test@telran.de";
        User mockUser = new User();
        mockUser.setEmail(email);
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    void getAllUsers() {
        List<User> mockUsers = Arrays.asList(new User(), new User(), new User());
        when(userRepository.getAllUsers()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        verify(userRepository).getAllUsers();
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUserName("OldName");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUserName("NewName");

        when(userRepository.findUserById(userId)).thenReturn(existingUser);
        userService.updateUser(userId, updatedUser);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("NewName", capturedUser.getUsername());
    }

    @Test
    void getUsersByStatus() {
        UserStatus status = UserStatus.OFFLINE;
        List<User> mockUsers = List.of(new User());

        when(userRepository.findUsersByStatus(status)).thenReturn(mockUsers);

        List<User> result = userService.getUsersByStatus(status);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(userRepository).findUsersByStatus(status);
    }

    @Test
    void isExistUserByEmail() {
        String email = "test@telran.de";
        boolean exists = true;

        when(userRepository.existsUserByEmail(email)).thenReturn(exists);

        boolean result = userService.isExistUserByEmail(email);

        assertTrue(result);
        verify(userRepository).existsUserByEmail(email);
    }

    @Test
    void toggleBlockUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setStatus(UserStatus.ONLINE);

        when(userRepository.findUserById(userId)).thenReturn(user);

        userService.toggleBlockUser(userId);

        assertEquals(UserStatus.BLOCKED, user.getStatus());
        verify(userRepository).save(user);

        userService.toggleBlockUser(userId);

        assertEquals(UserStatus.OFFLINE, user.getStatus());
        verify(userRepository, times(2)).save(user);
    }
}