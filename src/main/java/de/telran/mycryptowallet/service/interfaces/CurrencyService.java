package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.Currency;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * This interface defines the contract for managing currencies within the system.
 * It provides methods for adding new currencies, checking for the existence of currencies, retrieving currencies by code or title,
 * and getting information about basic and BTC (Bitcoin) currencies.
 *
 * @author Alexander Isai
 * @version 1.0
 * @since 2024-01-22
 */
public interface CurrencyService {
    /**
     * Adds a new currency to the system.
     *
     * @param currency The currency entity to be added.
     */
    void addCurrency(Currency currency);

    /**
     * Retrieves all currencies available in the system.
     *
     * @return A list of {@code Currency} entities.
     */
    List<Currency> getAllCurrencies();

    /**
     * Checks if a currency exists in the system by its code.
     *
     * @param code The unique code of the currency.
     * @return {@code true} if the currency exists, {@code false} otherwise.
     */
    Boolean isExistCurrencyByCode(String code);

    /**
     * Retrieves a currency by its unique code.
     *
     * @param code The code of the currency.
     * @return The {@code Currency} entity corresponding to the provided code.
     */
    Currency getCurrencyByCode(String code);

    /**
     * Retrieves a currency by its title.
     *
     * @param title The title of the currency.
     * @return The {@code Currency} entity corresponding to the provided title.
     */
    Currency getCurrencyByTitle(String title);

    /**
     * Gets the code of the basic currency used in the system.
     * The basic currency is typically used for default transactions and valuations.
     *
     * @return The code of the basic currency.
     */
    String getBasicCurrency();

    /**
     * Gets the code of the Bitcoin (BTC) currency.
     * This method is useful for operations specifically related to Bitcoin transactions.
     *
     * @return The code of the Bitcoin currency.
     */
    String getBTCCurrency();
}
