package de.telran.mycryptowallet.controller;
import de.telran.mycryptowallet.dto.accountDTO.AccountOutDTO;
import de.telran.mycryptowallet.dto.orderDTO.OrderOutDTO;
import de.telran.mycryptowallet.dto.userDTO.UserOutDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.mapper.accountMapper.AccountMapper;
import de.telran.mycryptowallet.mapper.orderMapper.OrderMapper;
import de.telran.mycryptowallet.mapper.userMapper.UserListMapper;
import de.telran.mycryptowallet.service.interfaces.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * The AdminController class manages administrative tasks such as user and account management,
 * order viewing, and application-wide balance and profit reporting. It provides endpoints for
 * blocking or unblocking users, changing user passwords, displaying all users and accounts,
 * showing user-specific accounts and orders, listing accounts by currency, reporting total balance
 * and profits, showing users by status, and resetting all account balances.
 * Each endpoint is annotated with Swagger documentation for ease of use in API exploration tools.
 *
 *  @author Alexander Isai
 *  @version 22.01.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
@Slf4j
public class AdminController {

    private final UserService userService;
    private final AccountService accountService;
    private final AccountBusinessService accountBusinessService;
    private final OrderService orderService;
    private final UserListMapper userListMapper;
    private final AccountMapper accountMapper;
    private final OrderMapper orderMapper;

    @GetMapping(value = "/show-all-users")
    @Operation(summary = "Show all users", description = "Returns detailed information about all users")
    public List<UserOutDTO> showAllUsers() {
        return userListMapper.toDtoList(userService.getAllUsers());
    }

    @PutMapping(value = "/block")
    @Operation(summary = "Block/unblock a user by ID", description = "Blocks a user by ID. The user is no longer able to make transactions with accounts")
    public void toggleBlockUser(@RequestParam(name = "id") Long id) {
        userService.toggleBlockUser(id);
    }

    @PutMapping(value = "/set-password/{id}")
    @Operation(summary = "Change the password for a user by ID", description = "Changes the password for the user specified by ID")
    public void changeUserPassword(@PathVariable(value = "id") Long id, @RequestParam(value = "password") String newPassword) {
        userService.changeUserPassword(id, newPassword);
    }

    @GetMapping(value = "/show-all-accounts")
    @Operation(summary = "Show all accounts", description = "Returns detailed information about all accounts")
    public List<AccountOutDTO> showAllAccounts() {
        return accountMapper.toDtoList(accountService.getAllAccounts());
    }

    @GetMapping(value = "/show-user-accounts")
    @Operation(summary = "show users accounts by ID", description = "Returns detailed accounts information for the user account specified by ID")
    public List<AccountOutDTO> showAccountsByUser(@RequestParam(name = "userId") Long userId) {
        return accountMapper.toDtoList(accountService.getAccountsByUser(userId));
    }

    @GetMapping(value = "/show-orders")
    @Operation(summary = "Show all orders by user", description = "Returns detailed information about all orders by user")
    public List<OrderOutDTO> showOrdersByUser(@RequestParam(name = "userId") Long userId) {
        return orderMapper.toDtoList(orderService.getUsersOrders(userId));
    }

    @GetMapping(value = "/accounts-currency")
    @Operation(summary = "Show all accounts by currency", description = "Returns detailed information about all accounts for a given currency")
    public List<AccountOutDTO> showAccountsByCurrency(@RequestParam(name = "code") String code) {
        return accountMapper.toDtoList(accountService.getAccountsByCurrency(code));
    }

    @GetMapping(value = "/show-total-balance")
    @Operation(summary = "Show balance by app", description = "Returns the total balance for the application (sum of all accounts) in dollar and bitcoin projections")
    public TotalUserBalance showAllBalances() {
        return accountBusinessService.getTotalBalance();
    }

    @GetMapping(value = "/profit")
    @Operation(summary = "Show the app's profits", description = "Returns the application's profit value in two projections - in dollars and in bitcoin")
    public TotalUserBalance showProfit() {
        return accountBusinessService.showProfit();
    }


    @GetMapping(value = "/users-with-status")
    @Operation(summary = "Show all users by status", description = "Returns detailed information about all users with the specified status")
    public List<UserOutDTO> showUsersByStatus(@RequestParam(name = "status") UserStatus status) {
        return userListMapper.toDtoList(userService.getUsersByStatus(status));
    }

    @GetMapping(value = "/accounts-reset")
    @Operation(summary = "Zero out all accounts", description = "Clears the balances of all accounts")
    public void resetAllAccounts() {
        orderService.cancelAllOrders();
        accountService.resetAllAccounts();
    }

    @GetMapping(value = "/orders-cancel")
    @Operation(summary = "Cancel all orders", description = "Cancel all orders")
    public void cancelAllOrders(){
        orderService.cancelAllOrders();
    }

}
