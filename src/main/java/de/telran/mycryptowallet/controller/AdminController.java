package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 03.02.2024.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
@Slf4j
public class AdminController {

    private final UserService userService;

    @GetMapping(value = "/show-all-users")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping(value = "block")
    public void toggleBlockUser(@RequestParam(name = "id") Long id){
        userService.toggleBlockUser(id);
    }
}
