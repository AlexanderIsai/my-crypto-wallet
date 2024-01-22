package de.telran.mycryptowallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


/**
 * Entity Account
 *
 * @author Alexander Isai on 16.01.2024.
 */

@Entity
@Table(name = "crypto_accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "public_address")
    private String publicAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency", referencedColumnName = "code")
    private Currency currency;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "account")
    private Set<Order> orders = new HashSet<>();


}
