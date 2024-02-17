package de.telran.mycryptowallet.controller;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.exceptions.EntityNotFoundException;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.UserService;
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
    public void save(@RequestBody @Valid User user) {
        userService.addNewUser(user);
        accountService.createUserAccounts(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> showUserById(@PathVariable(value = "id") Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            throw new EntityNotFoundException("User is not found");
        }
    }

    @GetMapping(value = "/active-user")
    public ResponseEntity<User> getActiveUser() {
        User user = activeUserService.getActiveUser();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else  {
            throw new EntityNotFoundException("User is not found");
        }
    }

    @GetMapping(value = "/email")
    public ResponseEntity<User> showUserByEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email).orElseThrow());
    }

    @PutMapping(value = "/{id}")
    public void updateUser(@PathVariable(value = "id") Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }
    @GetMapping(value = "/exist")
    public Boolean isExistUserByEmail(@RequestParam(name = "email") String email) {
        return userService.isExistUserByEmail(email);
    }
}
