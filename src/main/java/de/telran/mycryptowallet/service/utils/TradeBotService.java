package de.telran.mycryptowallet.service.utils;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.entity.Rate;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.service.interfaces.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * description
 *
 * @author Alexander Isai on 11.02.2024.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TradeBotService {
    private final UserService userService;
    private final CurrencyService currencyService;
    private final AccountService accountService;
    private final OperationService operationService;
    private final RateService rateService;
    private final TotalUserBalanceService totalUserBalanceService;
    private final AccountBusinessService accountBusinessService;
    private User MORNING_BUYER;
    private User EVENING_BUYER;
    private final int SCALE = 2;

    @PostConstruct
    public void init() {
        MORNING_BUYER = userService.getUserByEmail("morning@buy.btc").orElseThrow();
        EVENING_BUYER = userService.getUserByEmail("evening@buy.btc").orElseThrow();
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void eveningBuy(){
        buy(EVENING_BUYER);

    }
    @Scheduled(cron = "0 0 20 * * *")
    public void eveningSell(){
        sell(MORNING_BUYER);
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void morningBuy(){
        buy(MORNING_BUYER);
    }
    @Scheduled(cron = "0 0 8 * * *")
    public void morningSell(){
        sell(EVENING_BUYER);
    }

    public void buy(User user){
        Operation operationBuy = new Operation();
        operationBuy.setUser(user);
        operationBuy.setCurrency(currencyService.getCurrencyByCode(currencyService.getBTCCurrency()));
        Account accountBTCBuyer = accountService.getAccountByUserIdAndCurrency(user.getId(), currencyService.getBTCCurrency()).orElseThrow();
        Account accountUSDBuyer = accountService.getAccountByUserIdAndCurrency(user.getId(), currencyService.getBasicCurrency()).orElseThrow();
        operationBuy.setAccount(accountBTCBuyer);
        Rate operationRate = rateService.getFreshRate(currencyService.getBTCCurrency());
        operationBuy.setRateValue(operationRate.getValue());
        operationBuy.setAmount(accountUSDBuyer.getBalance().divide(operationRate.getValue(), SCALE, RoundingMode.HALF_DOWN).subtract(BigDecimal.valueOf(0.01)));
        operationBuy.setType(OperationType.BUY);
        operationService.cashFlow(operationBuy);
        totalUserBalanceService.add(accountBusinessService.getTotalUserBalance(user.getId()));
    }

    public void sell(User user){
        Operation operationSell = new Operation();
        operationSell.setUser(user);
        operationSell.setCurrency(currencyService.getCurrencyByCode(currencyService.getBTCCurrency()));
        Account accountBTCSeller = accountService.getAccountByUserIdAndCurrency(user.getId(), currencyService.getBTCCurrency()).orElseThrow();
        operationSell.setAccount(accountBTCSeller);
        Rate operationRate = rateService.getFreshRate(currencyService.getBTCCurrency());
        operationSell.setRateValue(operationRate.getValue());
        operationSell.setAmount(accountBTCSeller.getBalance());
        operationSell.setType(OperationType.SELL);
        operationService.cashFlow(operationSell);
        totalUserBalanceService.add(accountBusinessService.getTotalUserBalance(user.getId()));
    }
}
