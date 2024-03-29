package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.exceptions.IncorrectInputException;
import de.telran.mycryptowallet.repository.CurrencyRepository;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return currencyRepository.findAll();
    }

    @Override
    public Boolean isExistCurrencyByCode(String code) {
        return currencyRepository.existsCurrencyByCode(code);
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        if(!isExistCurrencyByCode(code)){
            throw new IncorrectInputException("Currency with this code is not exist");
        }
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
