package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.service.utils.validators.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ActiveUserServiceImplTest {

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private ActiveUserServiceImpl activeUserService;

    @Test
    void getActiveUser() {
        User activeUser = new User();
        activeUser.setId(1L);
        activeUser.setEmail("test@telran.de");
        activeUser.setStatus(UserStatus.ONLINE);
        activeUser.setRole(UserRole.ROLE_USER);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(activeUser, null, activeUser.getAuthorities());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        doNothing().when(userValidator).isUserBlock(any(UserStatus.class));
        User result = activeUserService.getActiveUser();
        verify(userValidator).isUserBlock(UserStatus.ONLINE);
        assertEquals(activeUser.getEmail(), result.getEmail());
    }
}