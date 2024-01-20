package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


/**
 * description
 *
 * @author Alexander Isai on 18.01.2024.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM crypto_accounts")
    List<Account> getAllAccounts();

    List<Account> findAccountsByUserId(Long userId);

    List<Account> findAccountsByCurrencyCode(String code);

    Optional<Account> findAccountByIdAndUserId(Long accountId, Long userId);

    Optional<Account> findAccountByPublicAddress(String address);

    List<Account> findAccountsByBalanceGreaterThan(BigDecimal amount);

    List<Account> findAccountsByBalanceLessThan(BigDecimal amount);

    Boolean existsAccountById(Long id);

    Boolean existsAccountByUserId(Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM crypto_accounts WHERE balance BETWEEN :from AND :to")
    List<Account> getAccountsBetween(@Param(value = "from") BigDecimal from, @Param(value = "to") BigDecimal to);

}
