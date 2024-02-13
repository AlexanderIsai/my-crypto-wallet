package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.repository.CurrencyRepository;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${app.basic.currency}")
    private String BASIC_CURRENCY;
    @Value("${app.btc.currency}")
    private String BTC;

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

    @Override
    public String getBTCCurrency() {
        return BTC;
    }

}
