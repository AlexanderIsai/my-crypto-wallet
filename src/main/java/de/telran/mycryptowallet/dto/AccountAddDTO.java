package de.telran.mycryptowallet.dto;

import lombok.Data;

/**
 * this DTO is to streamline the transfer of data from client-side to server-side operations,
 * ensuring that only relevant information is communicated for the task of adding a new account.
 *
 * <p>The {@code currencyCode} attribute holds the code of the currency associated with the new account.
 * @author Alexander Isai
 * @version 23.01.2024
 */
@Data
public class AccountAddDTO {

    /**
     * The currency code representing the currency of the account to be added.
     */
    private String currencyCode;
}
