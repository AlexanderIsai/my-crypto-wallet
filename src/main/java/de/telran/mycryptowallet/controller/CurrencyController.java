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
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/currency")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final AccountService accountService;

    @GetMapping(value = "/all")
    @Operation(summary = "Показать все валюты", description = "Возвращает список всех доступных валют")
    public List<Currency> showAllCurrencies(){
        return currencyService.getAllCurrencies();
    }

    @PostMapping(value = "/add-new-currency")
    @Operation(summary = "Добавить новую валюту", description = "Создает и сохраняет новую валюту. Создает счета в этой валюте для всех пользователей")
    public void addNewCurrency(@RequestBody Currency currency){
        currencyService.addCurrency(currency);
        accountService.addAccountsWithNewCurrency(currency.getCode());
    }

    @GetMapping(value = "/{code}")
    @Operation(summary = "Показать валюту по коду", description = "Возвращает информацию о валюте по ее коду")
    public ResponseEntity<Currency> showCurrencyByCode(@PathVariable(name = "code") String code){
        Currency currency = currencyService.getCurrencyByCode(code);
        if (currency != null) {
            return ResponseEntity.ok(currency);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping(value = "/currency-exist")
    @Operation(summary = "Проверить наличие валюты в БД по коду", description = "Проверяет существование валюты в БД")
    public Boolean isExistCurrencyByCode(@RequestParam(name = "code") String code){
        return currencyService.isExistCurrencyByCode(code);
    }
}
