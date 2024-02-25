package de.telran.mycryptowallet.controller;
import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.service.interfaces.AccountService;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * The CurrencyController class handles HTTP requests related to currency operations.
 * It provides endpoints for getting a list of all currencies, adding a new currency,
 * retrieving currency information by its code, and checking if a currency exists in the database.
 * This controller uses CurrencyService and AccountService to perform business logic operations.
 *
 * @author Alexander Isai
 * @since 22.01.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/currency")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final AccountService accountService;

    /**
     * Retrieves a list of all available currencies in the system.
     *
     * @return A list of Currency objects.
     */
    @GetMapping(value = "/all")
    @Operation(summary = "Show all currencies", description = "Returns a list of all available currencies")
    public List<Currency> showAllCurrencies(){
        return currencyService.getAllCurrencies();
    }

    /**
     * Adds a new currency to the system and creates accounts for all users in this new currency.
     *
     * @param currency The Currency object to be added.
     */
    @PostMapping(value = "/add-new-currency")
    @Operation(summary = "Add new currency", description = "Creates and saves a new currency. Creates accounts in this currency for all users")
    public void addNewCurrency(@RequestBody Currency currency){
        currencyService.addCurrency(currency);
        accountService.addAccountsWithNewCurrency(currency.getCode());
    }

    /**
     * Retrieves currency information by its code.
     *
     * @param code The currency code.
     * @return ResponseEntity with Currency object if found, otherwise returns ResponseEntity with not found status.
     */
    @GetMapping(value = "/{code}")
    @Operation(summary = "Show currency by code", description = "Returns currency information by currency code")
    public ResponseEntity<Currency> showCurrencyByCode(@PathVariable(name = "code") String code){
        Currency currency = currencyService.getCurrencyByCode(code);
        if (currency != null) {
            return ResponseEntity.ok(currency);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Checks if a currency exists in the database by its code.
     *
     * @param code The currency code.
     * @return True if the currency exists, false otherwise.
     */
    @GetMapping(value = "/currency-exist")
    @Operation(summary = "Check currency availability in the database by code", description = "Checks the existence of the currency in the database")
    public Boolean isExistCurrencyByCode(@RequestParam(name = "code") String code){
        return currencyService.isExistCurrencyByCode(code);
    }
}