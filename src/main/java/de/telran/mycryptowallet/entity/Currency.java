package de.telran.mycryptowallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


/**
 * Entity Currency
 *
 * @author Alexander Isai on 16.01.2024.
 */
@Entity
@Table(name = "crypto_currencies")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Currency {

    @Id
    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "title")
    private String title;

}
