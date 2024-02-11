package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.entity.TotalUserBalance;
import de.telran.mycryptowallet.repository.TotalUserBalanceRepository;
import de.telran.mycryptowallet.service.interfaces.TotalUserBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author Alexander Isai on 11.02.2024.
 */
@RequiredArgsConstructor
@Service
public class TotalUserBalanceServiceImpl implements TotalUserBalanceService {
    private final TotalUserBalanceRepository totalUserBalanceRepository;

    @Override
    public void add(TotalUserBalance totalUserBalance) {
        totalUserBalanceRepository.save(totalUserBalance);
    }
}
