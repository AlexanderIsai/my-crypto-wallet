package de.telran.mycryptowallet.repository;

import de.telran.mycryptowallet.entity.TotalUserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on {@link TotalUserBalance} entities.
 * This extends {@link JpaRepository} to allow standard data access functionalities for TotalUserBalance data.
 *
 * @see TotalUserBalance An entity that represents the total balance of a user in the system.
 * @author Alexander Isai
 * @version 11.02.2024
 */
public interface TotalUserBalanceRepository extends JpaRepository<TotalUserBalance, Long> {
}