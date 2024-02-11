package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.Currency;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
public interface CurrencyService {

    void addCurrency(Currency currency);
    List<Currency> getAllCurrencies();
    Boolean isExistCurrencyByCode(String code);
    Currency getCurrencyByCode(String code);
    Currency getCurrencyByTitle(String title);
    String getBasicCurrency();
    String getBTCCurrency();
}
