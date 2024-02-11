package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.TotalUserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * description
 *
 * @author Alexander Isai on 11.02.2024.
 */
public interface TotalUserBalanceRepository extends JpaRepository<TotalUserBalance, Long> {
}
