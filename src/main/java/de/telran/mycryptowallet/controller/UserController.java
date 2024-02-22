package de.telran.mycryptowallet.controller;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.exceptions.EntityNotFoundException;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * description
 *
 * @author Alexander Isai on 20.01.2024.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final ActiveUserService activeUserService;
    private final AccountService accountService;

    @PostMapping(value = "/add-new-user")
    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя и все возможные счета для него")
    public void save(@RequestBody @Valid User user) {
        userService.addNewUser(user);
        accountService.createUserAccounts(user);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает подробную информацию о пользователе по его уникальному идентификатору")
    public ResponseEntity<User> showUserById(@PathVariable(value = "id") Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            throw new EntityNotFoundException("User is not found");
        }
    }

    @GetMapping(value = "/active-user")
    @Operation(summary = "Получить активного пользователя", description = "Возращает подробную информацию об активном пользователе")
    public ResponseEntity<User> getActiveUser() {
        User user = activeUserService.getActiveUser();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else  {
            throw new EntityNotFoundException("User is not found");
        }
    }

    @GetMapping(value = "/email")
    @Operation(summary = "Получить пользователя по email", description = "Возвращает подробную информацию о пользователе по email")
    public ResponseEntity<User> showUserByEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email).orElseThrow());
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменить пользователя по ID", description = "Сохраняет изменения в объекте пользователь под соответствующим ID")
    public void updateUser(@PathVariable(value = "id") Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }
    @GetMapping(value = "/exist")
    @Operation(summary = "Проверяет наличие пользователя в БД по email", description = "Возвращает результат проверки наличия пользователя в БД по email")
    public Boolean isExistUserByEmail(@RequestParam(name = "email") String email) {
        return userService.isExistUserByEmail(email);
    }
}
