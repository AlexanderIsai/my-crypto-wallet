package de.telran.mycryptowallet.entity;

import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entity order
 *
 * @author Alexander Isai on 16.01.2024.
 */
@Data
@Entity
@Table(name = "crypto_orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency", referencedColumnName = "code")
    private Currency currency;


    @Column(name = "rate")
    private BigDecimal rateValue;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "fee")
    private BigDecimal orderFee;

}
