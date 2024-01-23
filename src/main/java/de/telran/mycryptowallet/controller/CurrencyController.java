package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
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

    @GetMapping(value = "/all")
    public List<Currency> showAllCurrencies(){
        return currencyService.getAllCurrencies();
    }

    @PostMapping(value = "/add-new-currency")
    public void addNewCurrency(@RequestBody Currency currency){
        currencyService.addCurrency(currency);
    }

    @GetMapping(value = "/{code}")
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
    public Boolean isExistCurrencyByCode(@RequestParam(name = "code") String code){
        return currencyService.isExistCurrencyByCode(code);
    }
}
