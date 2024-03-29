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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
        Map<String, Object> mockBtcRate = Map.of("currency", "BTC", "price", "50000");
        Map<String, Object> mockEthRate = Map.of("currency", "ETH", "price", "4000");
        when(rateGenerator.getBitcoinPrice()).thenReturn(mockBtcRate);
        when(rateGenerator.geEthereumPrice()).thenReturn(mockEthRate);


        List<Map<String, Object>> result = rateService.getRate();


        assertEquals(2, result.size(), "Должно быть две валюты");
        assertEquals(mockBtcRate, result.get(0), "Первая валюта должна быть BTC");
        assertEquals(mockEthRate, result.get(1), "Вторая валюта должна быть ETH");

    }

    @Test
    void getRate() {
        Map<String, Object> expectedBtcRate = Map.of("BTC", Map.of("last", "50000"));
        Map<String, Object> expectedEthRate = Map.of("ETH", Map.of("last", "4000"));

        when(rateGenerator.getBitcoinPrice()).thenReturn(expectedBtcRate);
        when(rateGenerator.geEthereumPrice()).thenReturn(expectedEthRate);

        List<Map<String, Object>> actualRates = rateService.getRate();

        assertThat(actualRates).hasSize(2);
        assertThat(actualRates).containsExactlyInAnyOrder(expectedBtcRate, expectedEthRate);
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