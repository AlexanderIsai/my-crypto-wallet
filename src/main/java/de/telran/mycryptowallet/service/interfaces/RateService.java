package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.Rate;

import java.math.BigDecimal;
import java.util.Map;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
public interface RateService {

    Map<String, Object> getRate();
    void addRate();

    Rate getFreshRate(String code);

    Rate setOrderRate(BigDecimal rateValue);

}
