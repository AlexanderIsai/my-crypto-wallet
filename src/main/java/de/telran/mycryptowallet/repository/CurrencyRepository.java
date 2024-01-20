package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 18.01.2024.
 */
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM crypto_currencies")
    List<Currency> getAllCurrencies();

    Boolean existsCurrencyByCode(String code);

    Optional<Currency> findCurrencyByCode(String code);

}
