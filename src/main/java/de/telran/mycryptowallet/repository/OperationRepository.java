package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for handling CRUD operations for {@link Operation} entities in the database.
 * It includes methods for retrieving all operations, operations by specific criteria like ID, user ID, account ID,
 * currency code, operation type, and operations with amounts within a certain range.
 *
 * @see Operation Entity representing a financial operation (e.g., buy, sell, deposit, withdraw).
 * @see JpaRepository Spring Data JPA repository for generic CRUD operations.
 * @author Alexander Isai
 * @version 18.01.2024
 */
public interface OperationRepository extends JpaRepository<Operation, Long> {

    /**
     * Finds operations associated with a specific user ID.
     *
     * @param userId the ID of the user.
     * @return a list of {@link Operation} entities related to the specified user.
     */
    List<Operation> findOperationsByUserId(Long userId);

    /**
     * Finds operations associated with a specific account ID.
     *
     * @param accountId the ID of the account.
     * @return a list of {@link Operation} entities related to the specified account.
     */
    List<Operation> findOperationsByAccountId(Long accountId);

    /**
     * Finds operations associated with a specific currency code.
     *
     * @param code the currency code.
     * @return a list of {@link Operation} entities with the given currency code.
     */
    List<Operation> findOperationsByCurrencyCode(String code);

    /**
     * Finds operations of a specific type.
     *
     * @param type the {@link OperationType}.
     * @return a list of {@link Operation} entities of the specified type.
     */
    List<Operation> findOperationsByType(OperationType type);

    /**
     * Finds operations with an amount greater than the specified value.
     *
     * @param amount the amount threshold.
     * @return a list of {@link Operation} entities with amounts greater than the specified value.
     */
    List<Operation> findOperationsByAmountGreaterThan(BigDecimal amount);

    /**
     * Finds operations with an amount less than the specified value.
     *
     * @param amount the amount threshold.
     * @return a list of {@link Operation} entities with amounts less than the specified value.
     */
    List<Operation> findOperationsByAmountLessThan(BigDecimal amount);

    /**
     * Retrieves operations with amounts within a specified range.
     *
     * @param from the starting amount of the range.
     * @param to the ending amount of the range.
     * @return a list of {@link Operation} entities with amounts within the specified range.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM crypto_operations WHERE amount BETWEEN :from AND :to")
    List<Operation> getOperationsBetween(@Param(value = "from") BigDecimal from, @Param(value = "to") BigDecimal to);

    /**
     * Finds operations associated with a specific user ID and currency code.
     *
     * @param userId the ID of the user.
     * @param code the currency code.
     * @return a list of {@link Operation} entities related to the specified user and currency.
     */
    List<Operation> findOperationsByUserIdAndCurrencyCode(Long userId, String code);
}



