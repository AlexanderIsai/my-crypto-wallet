package de.telran.mycryptowallet.service.impl;

import de.telran.mycryptowallet.dto.orderDTO.OrderTransferDTO;
import de.telran.mycryptowallet.dto.orderDTO.OrderAddDTO;
import de.telran.mycryptowallet.entity.*;
import de.telran.mycryptowallet.entity.entityEnum.OperationType;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.mapper.orderMapper.OrderMapper;
import de.telran.mycryptowallet.repository.OrderRepository;
import de.telran.mycryptowallet.service.interfaces.*;
import de.telran.mycryptowallet.service.utils.validators.AccountValidator;
import de.telran.mycryptowallet.service.utils.validators.OrderValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 25.01.2024.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ActiveUserService activeUserService;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final UserService userService;
    private final OperationService operationService;
    private final OrderValidator orderValidator;
    private final AccountValidator accountValidator;
    private final AccountBusinessService accountBusinessService;
    private final OrderMapper orderMapper;
    private final static double BALANCE_SAFETY = 0.01;
    private final static int SCALE = 2;

    @Override
    @Transactional
    public void addOrder(User user, OrderAddDTO orderAddDTO) {
        Order order = orderMapper.toEntity(orderAddDTO);

        order.setUser(user);

        Currency orderCurrency = currencyService.getCurrencyByCode(orderAddDTO.getCurrencyCode());
        order.setCurrency(orderCurrency);

        accountValidator.isCorrectNumber(order.getAmount());

        order.setStatus(OrderStatus.ACTIVE);

        accountBusinessService.reserveForOrder(user, orderAddDTO.getCurrencyCode(), order.getType(), order.getAmount(), order.getRateValue());
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getUsersOrders(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    @Override
    public void updateOrder(Long id, Order order) {
        order.setId(id);
        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    private BigDecimal calculateTransferAmount(Order order, Account executorOrderAccount, Account executorBasicAccount) {
        BigDecimal availableBalance = order.getType().equals(OperationType.BUY) ? executorOrderAccount.getBalance() : executorBasicAccount.getBalance().divide(order.getRateValue(), SCALE, RoundingMode.HALF_DOWN).subtract(BigDecimal.valueOf(BALANCE_SAFETY));
        BigDecimal requiredAmount = order.getAmount();
        if (availableBalance.compareTo(requiredAmount) >= 0) {
            order.setStatus(OrderStatus.DONE);
            return order.getAmount();
        } else {
            order.setStatus(OrderStatus.ACTIVE);
            BigDecimal actualTransferAmount = availableBalance.compareTo(order.getAmount()) < 0 ? availableBalance : order.getAmount().subtract(availableBalance);
            order.setAmount(order.getAmount().subtract(actualTransferAmount));
            return actualTransferAmount;
        }
    }

    private void orderTransferOperation(OrderTransferDTO orderTransferDTO){
        operationService.transfer(orderTransferDTO.getOwnerBasic(), orderTransferDTO.getExecutorBasic(), orderTransferDTO.getAmount().multiply(orderTransferDTO.getOrder().getRateValue()));// с долларового счета владельца ордера переводим бабло на долларовый счет исполнителя ордера
        operationService.transfer(orderTransferDTO.getExecutorOrder(), orderTransferDTO.getOwnerOrder(), orderTransferDTO.getAmount());
        operationService.addOrderOperation(orderTransferDTO.getOwner(), orderTransferDTO.getExecutor(), orderTransferDTO.getOrder(), orderTransferDTO.getAmount());
    }

    @Override
    @Transactional
    public void executeOrder(Long orderId) {
        Order order = getOrderById(orderId);// нужный ордер
        orderValidator.isOrderActive(order);
        User executorOrder = activeUserService.getActiveUser(); //юзер, который хочет его выполнить
        User ownerOrder = userService.getUserById(order.getUser().getId());//юзер, который владеет ордером
        Account ownerBasicAccount = accountService.getAccountByUserIdAndCurrency(ownerOrder.getId(), currencyService.getBasicCurrency()); //счет владельца ордера в базовой валюте (доллар), потому что он хочет купить крипту
        Account ownerOrderAccount = accountService.getAccountByUserIdAndCurrency(ownerOrder.getId(), order.getCurrency().getCode()); // счет владельца ордера в валюте ордера - туда должны упасть Битки

        Account executerBasicAccount = accountService.getAccountByUserIdAndCurrency(executorOrder.getId(), currencyService.getBasicCurrency()); //счет исполнителя ордера в базовой валюте (доллар), потому что он хочет продать крипту
        Account executerOrderAccount = accountService.getAccountByUserIdAndCurrency(executorOrder.getId(), order.getCurrency().getCode()); //счет исполнителя ордера в валюте ордера - сейчас Биток
        OperationType operationType = order.getType();

        cancelOrder(order.getId());
        BigDecimal transferAmount = calculateTransferAmount(order, executerOrderAccount, executerBasicAccount);
        OrderTransferDTO buyOrderTransferDTO = new OrderTransferDTO(order, ownerOrder, executorOrder, ownerBasicAccount, ownerOrderAccount, executerBasicAccount, executerOrderAccount, transferAmount);
        OrderTransferDTO sellOrderTransferDTO = new OrderTransferDTO(order, executorOrder, ownerOrder, executerBasicAccount, executerOrderAccount, ownerBasicAccount, ownerOrderAccount, transferAmount);

        if (operationType.equals(OperationType.BUY)){
            orderTransferOperation(buyOrderTransferDTO);
        }
        else {
            orderTransferOperation(sellOrderTransferDTO);
        }
            if(order.getStatus().equals(OrderStatus.ACTIVE)) {
                accountBusinessService.returnPartOrder(ownerOrderAccount, order.getAmount());
            }
        orderRepository.save(order);
    }
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        orderValidator.isOrderActive(order);
        order.setStatus(OrderStatus.CANCELLED);
        Account orderAccount = accountBusinessService.getAccountFromOrder(order);

        BigDecimal orderAmount = getOrderAmount(order);
        orderAccount.setOrderBalance(orderAccount.getOrderBalance().subtract(orderAmount));
        orderAccount.setBalance(orderAccount.getBalance().add(orderAmount));
        accountService.updateAccount(orderAccount.getId(), orderAccount);
        updateOrder(orderId, order);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus orderStatus) {
        return orderRepository.findOrdersByStatus(orderStatus);
    }

    @Override
    public List<Order> getOrdersByStatusTypeCurrency(OrderStatus status, OperationType type, String code) {
        Sort sort = type.equals(OperationType.BUY) ? Sort.by(Sort.Direction.DESC, "rateValue") : Sort.by(Sort.Direction.ASC, "rateValue");
        return orderRepository.findOrdersByStatusAndTypeAndCurrencyCode(status, type, code, sort);
    }

    @Override
    public List<Order> getOrdersByStatusAndType(OrderStatus status, OperationType type) {
        return orderRepository.findOrdersByStatusAndType(status, type);
    }

    @Override
    public BigDecimal getOrderAmount(Order order) {
        return order.getType().equals(OperationType.BUY) ? order.getAmount().multiply(order.getRateValue()) : order.getAmount();
    }

    @Override
    public void cancelAllOrders(){
        orderRepository.findAll().forEach(order -> cancelOrder(order.getId()));
    }
}
