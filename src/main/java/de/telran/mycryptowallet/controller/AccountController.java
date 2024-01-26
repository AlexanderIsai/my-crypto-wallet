package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.dto.AccountAddDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.service.interfaces.AccountService;
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


    @PostMapping(value = "/add")
    public void addNewAccount(@RequestBody AccountAddDTO accountAddDTO) {
        accountService.addNewAccount(accountAddDTO);
    }

    @GetMapping(value = "/all")
    public List<Account> showAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(value = "/user")
    public List<Account> showAccountsByUser(@RequestParam(name = "userId") Long userId) {
        return accountService.getAccountsByUser(userId);
    }

    @GetMapping(value = "/code")
    public List<Account> showAccountsByCurrency(@RequestParam(name = "code") String code) {
        return accountService.getAccountsByCurrency(code);
    }

    @GetMapping(value = "/user-currency")
    public Account showAccountByIdAndUserId(@RequestParam(name = "userId") Long userId, @RequestParam(name = "code") String code) {
        return accountService.getAccountByUserIdAndCurrency(userId, code).orElseThrow();
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
