package de.telran.mycryptowallet.entity;

import de.telran.mycryptowallet.entity.entityEnum.OperationStatus;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents a financial operation performed within the cryptocurrency wallet application.
 * This entity captures the details of various types of operations such as buying, selling,
 * depositing, or withdrawing cryptocurrencies.
 * Each operation is linked to a specific user, account, and currency to provide a detailed
 * context. It includes the amount of currency involved in the operation and, if applicable,
 * the rate at which the operation was performed.
 * @author Alexander Isai
 * @version 1.0
 * @since 18.01.2024
 * @see User
 * @see Account
 * @see Currency
 */
@Entity
@Data
@Table(name = "crypto_operations")
@AllArgsConstructor
@NoArgsConstructor
public class Operation {

    /**
     * The unique identifier for the operation.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who performed the operation.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The account associated with the operation.
     */
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    /**
     * The currency involved in the operation.
     */
    @ManyToOne
    @JoinColumn(name = "currency", referencedColumnName = "code")
    private Currency currency;

    /**
     * The amount of currency involved in the operation.
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * The exchange rate applied to the operation, if applicable.
     */
    @Column(name = "rate")
    private BigDecimal rateValue;

    /**
     * The type of operation, such as BUY, SELL, DEPOSIT, or WITHDRAW.
     */
    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType type;

    /**
     * The timestamp when the operation was created, automatically set upon creation.
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdOn;
}
