package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
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
public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM crypto_operations")
    List<Operation> getAllOperations();

    Optional<Operation> findOperationById(Long id);

    List<Operation> findOperationsByUserId(Long userId);

    List<Operation> findOperationsByAccountId(Long accountId);

    List<Operation> findOperationsByCurrencyCode(String code);

    List<Operation> findOperationsByType(OperationType type);

    List<Operation> findOperationsByAmountGreaterThan(BigDecimal amount);

    List<Operation> findOperationsByAmountLessThan(BigDecimal amount);

    @Query(nativeQuery = true, value = "SELECT * FROM crypto_operations WHERE amount BETWEEN :from AND :to")
    List<Operation> getOperationsBetween(@Param(value = "from") BigDecimal from, @Param(value = "to") BigDecimal to);

    List<Operation> findOperationsByUserIdAndCurrencyCode(Long userId, String code);


}
