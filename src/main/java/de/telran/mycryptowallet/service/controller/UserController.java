package de.telran.mycryptowallet.service.controller;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * description
 *
 * @author Alexander Isai on 20.01.2024.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/add-new-user")
    public void save(@RequestBody User user){
        userService.addNewUser(user);
    }


}
