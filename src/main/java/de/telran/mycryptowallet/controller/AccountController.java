package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.service.AccountService;
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

    @PutMapping(value = "/add")
    public void addNewAccount(Account account) {
        accountService.addNewAccount(account);
    }

    @GetMapping(value = "/all")
    public List<Account> showAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(value = "/{userId}")
    public List<Account> showAccountsByUser(@PathVariable(name = "userId") Long userId) {
        return accountService.getAccountsByUser(userId);
    }

    @GetMapping(value = "/{code}")
    public List<Account> showAccountsByCurrency(@PathVariable(name = "code") String code) {
        return accountService.getAccountsByCurrency(code);
    }

    @GetMapping(value = "/user-account")
    public Account showAccountByIdAndUserId(@RequestParam(name = "accId") Long accId, @RequestParam(name = "userId") Long userId) {
        return accountService.getAccountByIDAndUserId(accId, userId).orElseThrow();
    }

    @GetMapping(value = "/address")
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
    public void updateAccount(@PathVariable(name = "id") Long id, @RequestBody Account account) {
        accountService.updateAccount(id, account);
    }
}
