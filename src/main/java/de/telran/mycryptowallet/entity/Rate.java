package de.telran.mycryptowallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entity Rate
 *
 * @author Alexander Isai on 16.01.2024.
 */

@Data
@Entity
@Table(name = "crypto_rates")
@NoArgsConstructor
@AllArgsConstructor
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //TODO а надо ли мне этот айди?! кроме как для связи с другими таблицами?!

    @Column(name = "rate_value")
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "currency", referencedColumnName = "code")
    private Currency currency;

    @CreationTimestamp
    @Column(name =  "created_at")
    private Instant createdOn;
}
