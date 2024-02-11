package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
import de.telran.mycryptowallet.service.utils.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActiveUserServiceImplTest {

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private ActiveUserServiceImpl activeUserService;

    private User activeUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activeUser = new User();
        activeUser.setId(1L);
        activeUser.setEmail("test@example.com");
        activeUser.setStatus(UserStatus.ONLINE);
        activeUser.setRole(UserRole.ROLE_USER);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(activeUser, null, activeUser.getAuthorities());
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void getActiveUser() {
        doNothing().when(userValidator).isUserBlock(any(UserStatus.class));
        User result = activeUserService.getActiveUser();
        verify(userValidator).isUserBlock(UserStatus.ONLINE);
        assertEquals(activeUser.getEmail(), result.getEmail());
    }

    @Test
    void getActiveUser_WhenUserIsBlocked_ShouldThrowException() {
        doThrow(new UserIsBlockedException("User is blocked")).when(userValidator).isUserBlock(eq(UserStatus.BLOCKED));
        activeUser.setStatus(UserStatus.BLOCKED);
        assertThrows(UserIsBlockedException.class, () -> activeUserService.getActiveUser());
    }
}