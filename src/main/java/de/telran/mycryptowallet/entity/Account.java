package de.telran.mycryptowallet.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Entity Account
 * Represents an account within the cryptocurrency wallet application.
 * An account is associated with a user and a specific currency, and it tracks the available balance as well as the balance reserved for orders.
 * @author Alexander Isai on 16.01.2024.
 */

@Entity
@Table(name = "crypto_accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    /**
     * Unique identifier for the Account.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The public address of the Account, used for transactions.
     */
    @Column(name = "public_address")
    private String publicAddress;

    /**
     * The User entity associated with this Account.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The Currency entity representing the type of currency this Account holds.
     */
    @ManyToOne
    @JoinColumn(name = "currency", referencedColumnName = "code")
    private Currency currency;

    /**
     * The current balance of the Account.
     */
    @Column(name = "balance")
    private BigDecimal balance;

    /**
     * The balance of this Account that is reserved for pending orders.
     */
    @Column(name = "order_balance")
    private BigDecimal orderBalance;
}
