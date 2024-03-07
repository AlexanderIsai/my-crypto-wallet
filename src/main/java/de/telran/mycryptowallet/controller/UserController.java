package de.telran.mycryptowallet.controller;
import de.telran.mycryptowallet.dto.userDTO.UserAddDTO;
import de.telran.mycryptowallet.dto.userDTO.UserOutDTO;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.exceptions.EntityNotFoundException;
import de.telran.mycryptowallet.mapper.userMapper.UserMapper;
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
 * The UserController class handles web requests related to user operations.
 * It provides functionalities such as adding new users, displaying user information by ID or email,
 * updating user details, and checking if a user exists by their email address.
 *
 * @author Alexander Isai
 * @version 20.01.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final ActiveUserService activeUserService;
    private final AccountService accountService;
    private final UserMapper userMapper;

    /**
     * Creates a new user along with all possible accounts for that user.
     *
     * @param userAddDTO DTO containing user details to be added.
     */
    @PostMapping(value = "/add-new-user")
    @Operation(summary = "Add new user", description = "Creates a new user and all possible accounts for that user")
    public void save(@RequestBody @Valid UserAddDTO userAddDTO) {
        userService.addNewUser(userAddDTO);
        accountService.createUserAccounts(userService.getUserByEmail(userAddDTO.getEmail()));
    }

    /**
     * Retrieves detailed information about a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return ResponseEntity containing the user details.
     */
    @GetMapping(value = "/{id}")
    @Operation(summary = "Show user by ID", description = "Returns detailed information about the user by their unique identifier")
    public ResponseEntity<UserOutDTO> showUserById(@PathVariable(value = "id") Long id) {
        User user = userService.getUserById(id);
        UserOutDTO userOutDTO = userMapper.toDto(user);
        if (user != null) {
            return ResponseEntity.ok(userOutDTO);
        } else {
            throw new EntityNotFoundException("User is not found");
        }
    }

    /**
     * Returns detailed information about the currently active user.
     *
     * @return ResponseEntity containing the active user details.
     */
    @GetMapping(value = "/active-user")
    @Operation(summary = "Show active user", description = "Returns detailed information about the active user")
    public ResponseEntity<UserOutDTO> getActiveUser() {
        User user = activeUserService.getActiveUser();
        UserOutDTO userOutDTO = userMapper.toDto(user);
        if (user != null) {
            return ResponseEntity.ok(userOutDTO);
        } else {
            throw new EntityNotFoundException("User is not found");
        }
    }

    /**
     * Retrieves detailed information about a user by their email address.
     *
     * @param email The email address of the user.
     * @return ResponseEntity containing the user details.
     */
    @GetMapping(value = "/email")
    @Operation(summary = "Show user by e-mail", description = "Returns detailed information about the user by e-mail")
    public ResponseEntity<UserOutDTO> showUserByEmail(@RequestParam(value = "email") String email) {
        User user = userService.getUserByEmail(email);
        UserOutDTO userOutDTO = userMapper.toDto(user);
        if (user != null) {
            return ResponseEntity.ok(userOutDTO);
        } else {
            throw new EntityNotFoundException("User is not found");
        }
    }

    /**
     * Updates the details of an existing user identified by ID.
     *
     * @param id The unique identifier of the user.
     * @param user User object containing updated details.
     */
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update user by ID", description = "Saves the changes in the object to the user under the corresponding ID")
    public void updateUser(@PathVariable(value = "id") Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    /**
     * Checks if a user exists in the database by their email address.
     *
     * @param email The email address of the user to check.
     * @return Boolean indicating if the user exists.
     */
    @GetMapping(value = "/exist")
    @Operation(summary = "Checks if the user is in the database by email", description = "Returns the result of checking if the user is available in the database by email")
    public Boolean isExistUserByEmail(@RequestParam(name = "email") String email) {
        return userService.isExistUserByEmail(email);
    }
}