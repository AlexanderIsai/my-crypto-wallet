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
    @Override
    public Map<String, Object> getRate() {
        return RateGenerator.getBitcoinPrice();
    }

    @Override
    @Scheduled(cron = "0 */5 * * * *")
    public void addRate() {
        Rate rate = new Rate();
        String currencyTitle = getRate().keySet().iterator().next();
        Map<String, Object> priceUsd = (Map<String, Object>) getRate().get(currencyTitle);
        rate.setCurrency(currencyService.getCurrencyByTitle(currencyTitle));
        rate.setValue(BigDecimal.valueOf((Integer) priceUsd.get(priceUsd.keySet().iterator().next())));
        rateRepository.save(rate);
    }
}
