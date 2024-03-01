package de.telran.mycryptowallet.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * description
 *
 * @author Alexander Isai on 11.02.2024.
 */
@Entity
@Table(name = "total_balance")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TotalUserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "usd_balance")
    private BigDecimal usd;

    @Column(name = "btc_balance")
    private BigDecimal btc;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdOn;
}
