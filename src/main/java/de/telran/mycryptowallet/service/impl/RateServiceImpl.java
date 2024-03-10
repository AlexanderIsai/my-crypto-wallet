package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.Rate;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.repository.RateRepository;
import de.telran.mycryptowallet.service.interfaces.CurrencyService;
import de.telran.mycryptowallet.service.interfaces.RateService;
import de.telran.mycryptowallet.service.utils.RateGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final JdbcTemplate jdbcTemplate;
    private final static BigDecimal BASIC_RATE = BigDecimal.valueOf(1.00);
    private final static BigDecimal MARGIN = BigDecimal.valueOf(5.00);
    @Value("${app.exchange.fee}")
    private BigDecimal fee;
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
            BigDecimal profit = price.multiply(fee);

            rate.setValue(price);
            rate.setSellRate(rate.getValue().add(profit));
            rate.setBuyRate(rate.getValue().subtract(profit));
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
    public BigDecimal setTransferRate(Rate rate, OperationType type) {
        BigDecimal rateValue = switch (type) {
            case BUY -> rate.getSellRate();
            case SELL -> rate.getBuyRate();
            case SEND, RECEIVED -> rate.getValue();
            default -> BigDecimal.ZERO;
        };
        return rateValue;
    }

    @Override
    @Scheduled(cron = "0 0 21 * * *")
    public void deleteAllExceptLastTen() {
        jdbcTemplate.update(
                "DELETE FROM crypto_rates WHERE id NOT IN (SELECT id FROM (" +
                        " SELECT id FROM crypto_rates ORDER BY id DESC LIMIT 10) AS subquery)"
        );
    }
}
