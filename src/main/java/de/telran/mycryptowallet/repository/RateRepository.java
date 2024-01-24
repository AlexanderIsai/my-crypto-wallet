package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 18.01.2024.
 */
public interface RateRepository extends JpaRepository<Rate, Long> {


    @Query(nativeQuery = true, value = "SELECT * FROM crypto_rates")
    List<Rate> getAllRates();

    List<Rate> findRatesByCurrencyCode(String code);

    @Query(nativeQuery = true, value = "SELECT * FROM crypto_rates WHERE currency = :code ORDER BY id DESC LIMIT 1")
    Rate getFreshRate(@Param(value = "code") String code);

    List<Rate> findRatesByValueGreaterThan(BigDecimal value);

    List<Rate> findRatesByValueLessThan(BigDecimal value);
}
