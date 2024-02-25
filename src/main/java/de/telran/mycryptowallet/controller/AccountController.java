package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.dto.AccountAddDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
import de.telran.mycryptowallet.service.interfaces.AccountBusinessService;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/accounts")
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final ActiveUserService activeUserService;
    private final AccountBusinessService accountBusinessService;


    @PostMapping(value = "/add")
    @Operation(summary = "add new account", description = "Creates and saves an account in the database in the specified currency for the active user")
    public void addNewAccount(@RequestBody AccountAddDTO accountAddDTO) {
        accountService.addNewAccount(activeUserService.getActiveUser(), accountAddDTO.getCurrencyCode());
    }

    @GetMapping(value = "/my")
    @Operation(summary = "Show active user accounts", description = "Returns detailed information about the active user's accounts")
    public List<Account> showAccountsByUser() {
        return accountService.getAccountsByUser(activeUserService.getActiveUser().getId());
    }
    @GetMapping(value = "/user-currency")
    @Operation(summary = "Show account by user and currency", description = "Returns detailed account information by user and currency")
    public Account showAccountByIdAndUserId(@RequestParam(name = "userId") Long userId, @RequestParam(name = "code") String code) {
        return accountService.getAccountByUserIdAndCurrency(userId, code).orElseThrow();
    }

    @GetMapping(value = "/address")
    @Operation(summary = "Показать аккаунт по публичному адресу", description = "Возвращает подробную информацию об аккаунте по публичному адресу")
    public ResponseEntity<Account> showAccountByAddress(@RequestParam(name = "address") String address) {
        Account account = accountService.getAccountByPublicAddress(address);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменить аккаунт по ID", description = "Изменяет заданный по ID аккаунт")
    public void updateAccount(@PathVariable(name = "id") Long id, @RequestBody Account account) {
        accountService.updateAccount(id, account);
    }

    @GetMapping(value = "/total-balance")
    @Operation(summary = "Показать общий баланс активного пользователя", description = "Возвращает значение общего баланса активного пользователя в двух проекциях - долларе и битках")
    public TotalUserBalance showTotalUserBalance(){
        return accountBusinessService.getTotalUserBalance(activeUserService.getActiveUser().getId());
    }
}
