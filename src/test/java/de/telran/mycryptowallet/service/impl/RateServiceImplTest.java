package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.entity.Rate;
import de.telran.mycryptowallet.repository.RateRepository;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.utils.RateGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateServiceImplTest {
    @Mock
    private CurrencyService currencyService;

    @Mock
    private RateRepository rateRepository;

    @Mock
    private RateGenerator rateGenerator;

    @InjectMocks
    private RateServiceImpl rateService;

    private Currency currency;
    private Rate rate;

    @BeforeEach
    void setUp() {
        currency = new Currency("BTC", "Bitcoin");
        rate = new Rate();
        rate.setCurrency(currency);
        rate.setValue(BigDecimal.valueOf(50000));
        rate.setBuyRate(BigDecimal.valueOf(47500));
        rate.setSellRate(BigDecimal.valueOf(52500));
    }

    @Test
    void getRateTest() {
        Map<String, Object> expectedRate = new HashMap<>();
        expectedRate.put("USD", Map.of("last", 50000));
        when(rateGenerator.getBitcoinPrice()).thenReturn(expectedRate);

        Map<String, Object> result = rateService.getRate();

        verify(rateGenerator, times(1)).getBitcoinPrice();
        assert result.equals(expectedRate);
    }

    @Test
    void addRateTest() {
        when(rateGenerator.getBitcoinPrice()).thenReturn(Map.of("BTC", Map.of("last", 50000)));
        when(currencyService.getCurrencyByTitle(anyString())).thenReturn(currency);

        rateService.addRate();

        verify(rateRepository, times(1)).save(any(Rate.class));
    }

    @Test
    void getFreshRateTest() {
        when(currencyService.getBasicCurrency()).thenReturn("USDT");
        when(currencyService.getCurrencyByCode("USDT")).thenReturn(new Currency("USDT", "Tether"));
        when(rateRepository.getFreshRate("BTC")).thenReturn(rate);

        Rate basicRate = rateService.getFreshRate("USDT");
        Rate freshRate = rateService.getFreshRate("BTC");

        verify(rateRepository, times(1)).getFreshRate("BTC");
        assert basicRate.getValue().equals(BigDecimal.valueOf(1.00));
        assert freshRate.equals(rate);
    }

    @Test
    void setOrderRateTest() {
    }
}