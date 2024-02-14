package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.service.interfaces.*;
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
    private final AccountService accountService;
    private final AccountBusinessService accountBusinessService;
    private final OrderService orderService;

    @GetMapping(value = "/show-all-users")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping(value = "/block")
    public void toggleBlockUser(@RequestParam(name = "id") Long id){
        userService.toggleBlockUser(id);
    }

    @PutMapping(value = "/set-password/{id}")
    public void changeUserPassword(@PathVariable(value = "id") Long id, @RequestParam (value = "password") String newPassword){
        userService.changeUserPassword(id, newPassword);
    }

    @GetMapping(value = "/show-all-accounts")
    public List<Account> showAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(value = "/show-user-account")
    public List<Account> showAccountsByUser(@RequestParam(name = "userId") Long userId) {
        return accountService.getAccountsByUser(userId);
    }

    @GetMapping(value = "/show-orders")
    public List<Order> showOrdersByUser(@RequestParam(name = "userId") Long userId) {
        return orderService.getUsersOrders(userId);
    }

    @GetMapping(value = "/accounts-currency")
    public List<Account> showAccountsByCurrency(@RequestParam(name = "code") String code) {
        return accountService.getAccountsByCurrency(code);
    }

    @GetMapping(value = "/show-total-balance")
    public TotalUserBalance showAllBalances(){
        return accountBusinessService.getTotalBalance();
    }

    @GetMapping(value = "/profit")
    public TotalUserBalance showProfit(){
        return accountBusinessService.showProfit();
    }

}
