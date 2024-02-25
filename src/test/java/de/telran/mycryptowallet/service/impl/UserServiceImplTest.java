package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.dto.UserAddDTO;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.repository.UserRepository;
import de.telran.mycryptowallet.service.utils.validators.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addNewUser() {
        User user = new User();
        UserAddDTO userAddDTO = new UserAddDTO("user", "test@telran.de", "password");
        user.setPassword(userAddDTO.getPassword());

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        userService.addNewUser(userAddDTO.getName(), userAddDTO.getEmail(), userAddDTO.getPassword());

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
        when(userRepository.findUserByEmail(email)).thenReturn(mockUser);

        User result = userService.getUserByEmail(email);

        assertEquals(email, result.getEmail());
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
        User mockUser = new User();
        mockUser.setUserName("New");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);
        UserValidator mockUserValidator = Mockito.mock(UserValidator.class);
        UserServiceImpl userService = new UserServiceImpl(mockUserRepository, mockPasswordEncoder, mockUserValidator);

        userService.updateUser(userId, mockUser);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(mockUserRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(userId, savedUser.getId());
        assertEquals("New", savedUser.getUsername());
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