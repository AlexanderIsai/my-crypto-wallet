package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.Rate;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Provides functionality for managing and retrieving cryptocurrency exchange rates within the system.
 * Allows for fetching current rates, adding new rates, retrieving the most recent rate for a specific
 * cryptocurrency, and setting rates for orders based on provided values.
 * @author Alexander Isai on 22.01.2024.
 */
public interface RateService {
    /**
     * Retrieves the current exchange rates for all supported cryptocurrencies.
     * The rates are returned as a map where the key is the currency code and the value
     * is an object containing rate details.
     *
     * @return A map of currency codes to their corresponding rate details.
     */
    List<Map<String, Object>> getRate();

    /**
     * Adds a new rate to the system. The method specifics, including parameters and how
     * the rate is determined or sourced, are implementation-dependent.
     */
    void addRate();

    /**
     * Retrieves the most recent exchange rate for a specific cryptocurrency code.
     *
     * @param code The code of the cryptocurrency for which to retrieve the rate.
     * @return The most recent {@link Rate} instance for the specified cryptocurrency code.
     */
    Rate getFreshRate(String code);

    /**
     * Sets the exchange rate for an order based on a provided value. This method can be
     * used to manually specify a rate for a transaction, overriding automatic rate retrieval.
     *
     * @param rateValue The value to set as the exchange rate.
     * @return The {@link Rate} instance created or updated with the specified value.
     */
    BigDecimal setTransferRate(Rate rate, OperationType type);

    public void deleteAllExceptLastTen();
}
