package de.telran.mycryptowallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents the exchange rate for a cryptocurrency in the system. This entity stores
 * information about the current value, buy rate, and sell rate of a specific cryptocurrency,
 * as well as its association with a specific currency entity.
 * Each rate is timestamped upon creation to track the historical changes in value over time.
 * This information can be used for various financial calculations, reporting, and to support
 * trading decisions within the cryptocurrency wallet platform.
 * @author Alexander Isai
 * @version 16.01.2024
 */

@Data
@Entity
@Table(name = "crypto_rates")
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    /**
     * Unique identifier for the Rate entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The current value of the cryptocurrency.
     */
    @Column(name = "rate_value")
    private BigDecimal value;

    /**
     * The rate at which the cryptocurrency can be bought in the system.
     */
    @Column(name = "buy_rate")
    private BigDecimal buyRate;

    /**
     * The rate at which the cryptocurrency can be sold in the system.
     */
    @Column(name = "sell_rate")
    private BigDecimal sellRate;

    /**
     * The currency associated with this rate.
     */
    @ManyToOne
    @JoinColumn(name = "currency", referencedColumnName = "code")
    private Currency currency;

    /**
     * The timestamp when this rate was created, automatically set at the moment of creation.
     */
    @CreationTimestamp
    @Column(name =  "created_at")
    private Instant createdOn;
}
