package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Rate;
import de.telran.mycryptowallet.repository.RateRepository;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.interfaces.RateService;
import de.telran.mycryptowallet.service.utils.RateGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description
 *
 * @author Alexander Isai on 23.01.2024.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RateServiceImpl implements RateService {

    private final CurrencyService currencyService;
    private final RateRepository rateRepository;
    private final RateGenerator rateGenerator;
    private final static BigDecimal BASIC_RATE = BigDecimal.valueOf(1.00);
    private final static BigDecimal MARGIN = BigDecimal.valueOf(5.00);
    private final static int SCALE = 2;
    @Override
    public List<Map<String, Object>> getRate() {
        List<Map<String, Object>> currencyRates = new ArrayList<>();

        Map<String, Object> rateBtc = rateGenerator.getBitcoinPrice();
        Map<String, Object> rateEth = rateGenerator.geEthereumPrice();

        currencyRates.add(rateBtc);
        currencyRates.add(rateEth);

        return currencyRates;
    }

    @Override
    @Scheduled(cron = "0 */5 * * * *")
    public void addRate() {
        List<Map<String, Object>> rates = getRate();
        rates.forEach(element -> {
            Rate rate = new Rate();
            String currencyTitle = element.keySet().iterator().next();
            Map<String, Object> priceUsd = (Map<String, Object>) element.get(currencyTitle);
            rate.setCurrency(currencyService.getCurrencyByTitle(currencyTitle));

            Object priceValue = priceUsd.get(priceUsd.keySet().iterator().next());
            BigDecimal price = new BigDecimal(priceValue.toString());

            rate.setValue(price);
            rate.setSellRate(rate.getValue().add(rate.getValue().multiply(MARGIN).divide(BigDecimal.valueOf(100),SCALE, RoundingMode.HALF_DOWN )));
            rate.setBuyRate(rate.getValue().subtract(rate.getValue().multiply(MARGIN).divide(BigDecimal.valueOf(100), SCALE, RoundingMode.HALF_DOWN)));
            rateRepository.save(rate);
        });
    }

    @Override
    public Rate getFreshRate(String code) {
        Rate rate = new Rate();
        if (code.equals(currencyService.getBasicCurrency())){
            rate.setCurrency(currencyService.getCurrencyByCode(currencyService.getBasicCurrency()));
            rate.setValue(BASIC_RATE);
            rate.setBuyRate(BASIC_RATE);
            rate.setSellRate(BASIC_RATE);
        }
        else {
            rate = rateRepository.getFreshRate(code);
        }
        return rate;
    }

    @Override
    public Rate setOrderRate(BigDecimal rateValue) {
        return null;
    }
}
