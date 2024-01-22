package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping(value = "/add-new-user")
    public void save(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @GetMapping(value = "/show-all-users")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
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
    public ResponseEntity<User> showActiveUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping(value = "/{email}")
    public ResponseEntity<User> showUserByEmail(@PathVariable(value = "email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email).orElseThrow());
    }

    @PutMapping(value = "/{id}")
    public void updateUser(@PathVariable(value = "id") Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @GetMapping(value = "/users-by-status")
    public List<User> showUsersByStatus(@RequestParam(name = "status") UserStatus status) {
        return userService.getUsersByStatus(status);
    }

    @GetMapping(value = "/user-exist")
    public Boolean isExistUserByEmail(@RequestParam(name = "email") String email) {
        return userService.isExistUserByEmail(email);
    }
}
