package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {
    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private Currency currencyUSD, currencyBTC;

    @BeforeEach
    void setUp() {
        currencyUSD = new Currency("USDT", "Tether");
        currencyBTC = new Currency("BTC", "Bitcoin");
    }

    @Test
    void addCurrency() {
        currencyService.addCurrency(currencyUSD);
        verify(currencyRepository, times(1)).save(currencyUSD);
    }

    @Test
    void getAllCurrencies() {
        when(currencyRepository.getAllCurrencies()).thenReturn(Arrays.asList(currencyUSD, currencyBTC));
        List<Currency> currencies = currencyService.getAllCurrencies();
        assertThat(currencies).hasSize(2);
        verify(currencyRepository, times(1)).getAllCurrencies();
    }

    @Test
    void isExistCurrencyByCode() {
        when(currencyRepository.existsCurrencyByCode("USDT")).thenReturn(true);
        Boolean exists = currencyService.isExistCurrencyByCode("USDT");
        assertThat(exists).isTrue();
        verify(currencyRepository, times(1)).existsCurrencyByCode("USDT");
    }

    @Test
    void getCurrencyByCode() {
        when(currencyRepository.findCurrencyByCode("USDT")).thenReturn(currencyUSD);
        Currency foundCurrency = currencyService.getCurrencyByCode("USDT");
        assertThat(foundCurrency).isEqualTo(currencyUSD);
        verify(currencyRepository, times(1)).findCurrencyByCode("USDT");
    }

    @Test
    void getCurrencyByTitle() {
        when(currencyRepository.findCurrencyByTitleContainsIgnoreCase("Tether")).thenReturn(currencyUSD);
        Currency foundCurrency = currencyService.getCurrencyByTitle("Tether");
        assertThat(foundCurrency).isEqualTo(currencyUSD);
        verify(currencyRepository, times(1)).findCurrencyByTitleContainsIgnoreCase("Tether");
    }

}