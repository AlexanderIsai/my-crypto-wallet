package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.TotalUserBalance;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 14.02.2024.
 */
public interface AccountManagerService {

    void depositManager(String code, BigDecimal amount);
    void withdrawManager(String code, BigDecimal amount);
    void buyManager(String code, BigDecimal amount);
    void sellManager(String code, BigDecimal amount);


}
