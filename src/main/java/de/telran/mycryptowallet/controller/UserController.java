package de.telran.mycryptowallet.controller;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @PostMapping(value = "/add-new-user")
    public void save(@RequestBody @Valid User user) {
        userService.addNewUser(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> showUserById(@PathVariable(value = "id") Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping(value = "/active-user")
    public ResponseEntity<User> getActiveUser() {
        User user = activeUserService.getActiveUser();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping(value = "/email")
    public ResponseEntity<User> showUserByEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email).orElseThrow());
    }
    //TODO доработать

    @PutMapping(value = "/{id}")
    public void updateUser(@PathVariable(value = "id") Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @GetMapping(value = "/status")
    public List<User> showUsersByStatus(@RequestParam(name = "status") UserStatus status) {
        return userService.getUsersByStatus(status);
    }

    @GetMapping(value = "/exist")
    public Boolean isExistUserByEmail(@RequestParam(name = "email") String email) {
        return userService.isExistUserByEmail(email);
    }

    @PutMapping(value = "block")
    public void toggleBlockUser(@RequestParam(name = "id") Long id){
        userService.toggleBlockUser(id);
    }
}
