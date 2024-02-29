package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for handling CRUD operations on {@link Currency} entities within the database.
 * It provides methods to retrieve all currencies, check for the existence of a currency by its code, and find a currency by its code or title.
 *
 * @see Currency Entity representing a cryptocurrency or fiat currency.
 * @see JpaRepository Spring Data JPA repository for generic CRUD operations.
 * @author Alexander Isai
 * @version 18.01.2024
 */
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * Retrieves all currency entities from the database.
     *
     * @return a list of all {@link Currency} entities.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM crypto_currencies")
    List<Currency> getAllCurrencies();

    /**
     * Checks if a currency exists by its unique code.
     *
     * @param code the code of the currency.
     * @return true if the currency exists, false otherwise.
     */
    Boolean existsCurrencyByCode(String code);

    /**
     * Finds a currency by its unique code.
     *
     * @param code the code of the currency.
     * @return the found {@link Currency} entity.
     */
    Currency findCurrencyByCode(String code);

    /**
     * Finds a currency by a title that contains the specified string, case-insensitively.
     *
     * @param title the title or part of the title of the currency.
     * @return the found {@link Currency} entity.
     */
    Currency findCurrencyByTitleContainsIgnoreCase(String title);
}