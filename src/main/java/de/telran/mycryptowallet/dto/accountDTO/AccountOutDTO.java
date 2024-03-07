package de.telran.mycryptowallet.dto.accountDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * description
 *
 * @author Alexander Isai on 04.03.2024.
 */
@Data
@AllArgsConstructor
public class AccountOutDTO {

    private BigInteger id;
    private String code;
    private String publicAddress;
    private BigDecimal balance;
    private BigDecimal orderBalance;
}
