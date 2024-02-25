package de.telran.mycryptowallet.controller;
import de.telran.mycryptowallet.dto.AccountAddDTO;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.TotalUserBalance;
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
 * The AccountController class provides REST-ful web services for managing accounts
 * within the cryptocurrency wallet application. It offers functionalities to add new accounts,
 * view accounts by user, account by user and currency, account by public address, update accounts,
 * and show the total balance of the active user.
 *
 * @author Alexander Isai
 * @version 22.01.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/accounts")
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final ActiveUserService activeUserService;
    private final AccountBusinessService accountBusinessService;

    /**
     * Creates and saves a new account in the specified currency for the active user.
     *
     * @param accountAddDTO Data Transfer Object containing the currency code for the new account.
     */
    @PostMapping(value = "/add")
    @Operation(summary = "Add new account", description = "Creates and saves an account in the database in the specified currency for the active user.")
    public void addNewAccount(@RequestBody AccountAddDTO accountAddDTO) {
        accountService.addNewAccount(activeUserService.getActiveUser(), accountAddDTO.getCurrencyCode());
    }

    /**
     * Returns a list of accounts owned by the active user.
     *
     * @return List of {@link Account} objects.
     */
    @GetMapping(value = "/my")
    @Operation(summary = "Show active user accounts", description = "Returns detailed information about the active user's accounts.")
    public List<Account> showAccountsByUser() {
        return accountService.getAccountsByUser(activeUserService.getActiveUser().getId());
    }

    /**
     * Returns detailed account information for a given user ID and currency code.
     *
     * @param userId User ID.
     * @param code Currency code.
     * @return ResponseEntity containing the account or a not found response.
     */
    @GetMapping(value = "/user-currency")
    @Operation(summary = "Show account by user and currency", description = "Returns detailed account information by user and currency.")
    public ResponseEntity<Account> showAccountByIdAndUserId(@RequestParam(name = "userId") Long userId, @RequestParam(name = "code") String code) {
        Account account = accountService.getAccountByUserIdAndCurrency(userId, code);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    /**
     * Returns account information for a given public address.
     *
     * @param address The public address of the account.
     * @return ResponseEntity containing the account or a not found response.
     */
    @GetMapping(value = "/address")
    @Operation(summary = "Show account by public address", description = "Returns detailed information about the account at the public address.")
    public ResponseEntity<Account> showAccountByAddress(@RequestParam(name = "address") String address) {
        Account account = accountService.getAccountByPublicAddress(address);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    /**
     * Updates the information of an existing account specified by ID.
     *
     * @param id The ID of the account to update.
     * @param account The new account information.
     */
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update account by ID", description = "Modifies the account specified by ID.")
    public void updateAccount(@PathVariable(name = "id") Long id, @RequestBody Account account) {
        accountService.updateAccount(id, account);
    }

    /**
     * Shows the total balance of the active user in both dollars and bitcoins.
     *
     * @return ResponseEntity containing the total user balance or a not found response.
     */
    @GetMapping(value = "/total-balance")
    @Operation(summary = "Show the total balance of the active user", description = "Returns the value of the active user's total balance in two projections - dollars and bitcoins.")
    public ResponseEntity<TotalUserBalance> showTotalUserBalance() {
        TotalUserBalance totalUserBalance = accountBusinessService.getTotalUserBalance(activeUserService.getActiveUser().getId());
        return totalUserBalance != null ? ResponseEntity.ok(totalUserBalance) : ResponseEntity.notFound().build();
    }
}