package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for handling operations related to {@link Rate} entities.
 * Offers methods to retrieve and manipulate currency rates within the database, including fetching all rates,
 * rates by currency code, the most recent rate for a currency, and rates based on value comparisons.
 *
 * @see Rate Entity representing a currency exchange rate.
 * @see JpaRepository Spring Data JPA repository for generic CRUD operations.
 * @author Alexander Isai
 * @version 18.01.2024
 */
public interface RateRepository extends JpaRepository<Rate, Long> {

    /**
     * Finds rates by their associated currency code.
     *
     * @param code the currency code.
     * @return a list of {@link Rate} with the given currency code.
     */
    List<Rate> findRatesByCurrencyCode(String code);

    /**
     * Retrieves the most recent rate for a specific currency.
     *
     * @param code the currency code.
     * @return the latest {@link Rate} for the given currency code.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM crypto_rates WHERE currency = :code ORDER BY id DESC LIMIT 1")
    Rate getFreshRate(@Param(value = "code") String code);

    /**
     * Finds rates with a value greater than the specified amount.
     *
     * @param value the value to compare against.
     * @return a list of {@link Rate} greater than the specified value.
     */
    List<Rate> findRatesByValueGreaterThan(BigDecimal value);

    /**
     * Finds rates with a value less than the specified amount.
     *
     * @param value the value to compare against.
     * @return a list of {@link Rate} less than the specified value.
     */
    List<Rate> findRatesByValueLessThan(BigDecimal value);
}
