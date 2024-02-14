package de.telran.mycryptowallet.service.interfaces;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
public interface AccountService {

    void addNewAccount(User user, String code);
    List<Account> getAllAccounts();
    List<Account> getAccountsByUser(Long userId);
    List<Account> getAccountsByCurrency(String code);
    Account getAccountByPublicAddress(String address);
    Account getAccountById(Long id);
    List<Account> getAccountsByBalanceGreaterThan(BigDecimal amount);
    List<Account> getAccountsByBalanceLessThan(BigDecimal amount);
    Boolean existsAccountById(Long id);
    Boolean existsAccountByUserIdAndCurrency(Long userId, String code);
    void updateAccount(Long id, Account account);
    List<Account> getAccountsBetweenAmount(BigDecimal from, BigDecimal to);
    Optional<Account> getAccountByUserIdAndCurrency(Long userId, String code);
    void createUserAccounts(User user);
}
