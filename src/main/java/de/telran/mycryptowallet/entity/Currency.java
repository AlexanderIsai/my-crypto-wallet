package de.telran.mycryptowallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class represents a cryptocurrency entity within the system.
 * It encapsulates details about a specific cryptocurrency, including its code and title.
 * Each cryptocurrency is uniquely identified by its code. The title provides
 * a human-readable name for the cryptocurrency.
 * @author Alexander Isai on 16.01.2024.
 */
@Entity
@Table(name = "crypto_currencies")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Currency {

    /**
     * The unique code of the cryptocurrency.
     * This code is used as the primary key in the database table.
     */
    @Id
    @Column(name = "code", unique = true)
    private String code;

    /**
     * The title or name of the cryptocurrency.
     * This field provides a human-readable name for the currency.
     */
    @Column(name = "title")
    private String title;

}
