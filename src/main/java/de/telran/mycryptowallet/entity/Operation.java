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
 * Entity Operation
 *
 * @author Alexander Isai on 18.01.2024.
 */
@Entity
@Data
@Table(name = "crypto_operations")
@AllArgsConstructor
@NoArgsConstructor
public class Operation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "currency", referencedColumnName = "code")
    private Currency currency;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "rate")
    private BigDecimal rateValue;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType type;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdOn;


}
