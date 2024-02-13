package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.dto.AccountAddDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
import de.telran.mycryptowallet.service.interfaces.AccountBusinessService;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
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
    public void addNewAccount(@RequestBody AccountAddDTO accountAddDTO) {

        accountService.addNewAccount(accountAddDTO.getCurrencyCode());
    }

    @GetMapping(value = "/my")
    public List<Account> showAccountsByUser() {
        return accountService.getAccountsByUser(activeUserService.getActiveUser().getId());
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

    @GetMapping(value = "/total-balance")
    public TotalUserBalance showTotalUserBalance(){
        return accountBusinessService.getTotalUserBalance(activeUserService.getActiveUser().getId());
    }
}
