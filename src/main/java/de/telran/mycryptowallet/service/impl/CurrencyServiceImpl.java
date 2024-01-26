package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.repository.CurrencyRepository;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final String BASIC_CURRENCY = "USDT";

    @Override
    public void addCurrency(Currency currency) {
        currencyRepository.save(currency);
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.getAllCurrencies();
    }

    @Override
    public Boolean isExistCurrencyByCode(String code) {
        return currencyRepository.existsCurrencyByCode(code);
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findCurrencyByCode(code);
    }

    @Override
    public Currency getCurrencyByTitle(String title) {
        return currencyRepository.findCurrencyByTitleContainsIgnoreCase(title);
    }

    @Override
    public String getBasicCurrency() {
        return BASIC_CURRENCY;
    }
}
