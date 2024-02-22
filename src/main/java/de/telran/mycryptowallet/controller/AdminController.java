package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.service.interfaces.*;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Показать всех юзеров", description = "Возвращает подробную информацию обо всех юзерах")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping(value = "/block")
    @Operation(summary = "Заблокировать пользователя по ID", description = "Блокирует пользователя по ID. Пользователю более недоступны операции со счетами")
    public void toggleBlockUser(@RequestParam(name = "id") Long id){
        userService.toggleBlockUser(id);
    }

    @PutMapping(value = "/set-password/{id}")
    @Operation(summary = "Изменить пароль пользователю по ID", description = "Меняет пароль заданному по ID пользователю.")
    public void changeUserPassword(@PathVariable(value = "id") Long id, @RequestParam (value = "password") String newPassword){
        userService.changeUserPassword(id, newPassword);
    }

    @GetMapping(value = "/show-all-accounts")
    @Operation(summary = "Показать все аккаунты", description = "Возвращает подробную информацию обо всех аккаунтах")
    public List<Account> showAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(value = "/show-user-account")
    @Operation(summary = "Показать аккаунт пользователя по ID", description = "Возвращает подробную информацию об аккаунте пользователя, заданного по ID")
    public List<Account> showAccountsByUser(@RequestParam(name = "userId") Long userId) {
        return accountService.getAccountsByUser(userId);
    }

    @GetMapping(value = "/show-orders")
    @Operation(summary = "Показать все ордера", description = "Возвращает подробную информацию обо всех ордерах")
    public List<Order> showOrdersByUser(@RequestParam(name = "userId") Long userId) {
        return orderService.getUsersOrders(userId);
    }

    @GetMapping(value = "/accounts-currency")
    @Operation(summary = "Показать все аккаунты по валюте", description = "Возвращает подробную информацию обо всех аккаунтах по заданной валюте")
    public List<Account> showAccountsByCurrency(@RequestParam(name = "code") String code) {
        return accountService.getAccountsByCurrency(code);
    }

    @GetMapping(value = "/show-total-balance")
    @Operation(summary = "Показать баланс по приложению", description = "Возвращает общий баланс по приложению (сумма всех аккантов) в проекциях доллара и биткойна")
    public TotalUserBalance showAllBalances(){
        return accountBusinessService.getTotalBalance();
    }

    @GetMapping(value = "/profit")
    @Operation(summary = "Показать прибыль приложения", description = "Возвращает значение прибыли приложения в двух проекциях - в долларе и в биткойне")
    public TotalUserBalance showProfit(){
        return accountBusinessService.showProfit();
    }


    @GetMapping(value = "/users-with-status")
    @Operation(summary = "Показать всех юзеров по статусу", description = "Возвращает подробную информацию обо всех юзерах с заданным статусом")
    public List<User> showUsersByStatus(@RequestParam(name = "status") UserStatus status) {
        return userService.getUsersByStatus(status);
    }

    @GetMapping(value = "/accounts-reset")
    @Operation(summary = "Обнулить все аккаунты", description = "Обнуляет балансы всех аккаунтов")
    public void resetAllAccounts(){
        accountService.resetAllAccounts();
    }

}
