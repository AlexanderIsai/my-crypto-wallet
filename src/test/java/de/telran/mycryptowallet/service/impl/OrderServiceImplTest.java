package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.entity.Account;
import de.telran.mycryptowallet.entity.Currency;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.repository.OrderRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import de.telran.mycryptowallet.service.utils.validators.OrderValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ActiveUserService activeUserService;
    @Mock
    private AccountService accountService;
    @Mock
    private CurrencyService currencyService;
    @Mock
    private UserService userService;
    @Mock
    private OperationService operationService;
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private AccountValidator accountValidator;
    @Mock
    private AccountBusinessService accountBusinessService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void addOrderTest() {
        User user = mock(User.class);
        String code = "BTC";
        OperationType type = OperationType.BUY;
        BigDecimal amount = new BigDecimal("1.0");
        BigDecimal rate = new BigDecimal("50000");

        Currency currency = new Currency();
        currency.setCode(code);

        when(currencyService.getCurrencyByCode(code)).thenReturn(currency);

        orderService.addOrder(user, code, type, amount, rate);

        verify(accountValidator).isCorrectNumber(amount);
        verify(accountBusinessService).reserveForOrder(user, code, type, amount, rate);
        verify(orderRepository).save(any(Order.class));
    }

}