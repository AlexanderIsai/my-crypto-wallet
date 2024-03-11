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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

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
    private final ManagerUserService managerUserService;
    @Value("${app.transfer.fee}")
    private BigDecimal fee;
    private final static BigDecimal BALANCE_SAFETY = BigDecimal.valueOf(0.01);
    private final static int SCALE = 2;

    @Override
    @Transactional
    public void addOrder(User user, OrderAddDTO orderAddDTO) {
        Order order = orderMapper.toEntity(orderAddDTO);

        order.setUser(user);

        Currency orderCurrency = currencyService.getCurrencyByCode(orderAddDTO.getCurrencyCode());
        order.setCurrency(orderCurrency);

        accountValidator.isCorrectNumber(order.getAmount());

        BigDecimal orderFee = order.getAmount().multiply(fee).multiply(order.getRateValue());
        order.setOrderFee(orderFee);

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
        BigDecimal orderRate = order.getRateValue();
        BigDecimal orderFee = order.getAmount().multiply(fee);
        BigDecimal availableBalanceOrderCurrency = executorOrderAccount.getBalance();
        BigDecimal availableBalanceUSD = (executorBasicAccount.getBalance().divide(orderRate, SCALE, RoundingMode.HALF_DOWN)).subtract(orderFee);
        BigDecimal availableBalance = order.getType().equals(OperationType.BUY) ? availableBalanceOrderCurrency : availableBalanceUSD;
        BigDecimal requiredAmount = order.getType().equals(OperationType.BUY) ? order.getAmount() : order.getAmount().add(orderFee);
        if (availableBalance.compareTo(requiredAmount) >= 0) {
            order.setStatus(OrderStatus.DONE);
            return order.getAmount();
        } else {
            order.setStatus(OrderStatus.ACTIVE);
            order.setAmount(order.getAmount().subtract(availableBalance));
            return availableBalance;
        }
    }

    private void orderTransferOperation(OrderTransferDTO orderTransferDTO){
        operationService.transferByOrder(orderTransferDTO.getOwnerBasic(), orderTransferDTO.getExecutorBasic(), orderTransferDTO.getAmount().multiply(orderTransferDTO.getOrder().getRateValue()));// с долларового счета владельца ордера переводим бабло на долларовый счет исполнителя ордера
        operationService.transferByOrder(orderTransferDTO.getExecutorOrder(), orderTransferDTO.getOwnerOrder(), orderTransferDTO.getAmount());
        operationService.addOrderOperation(orderTransferDTO.getOwner(), orderTransferDTO.getExecutor(), orderTransferDTO.getOrder(), orderTransferDTO.getAmount());
    }

    @Override
    @Transactional
    public void executeOrder(Long orderId) {
        Order order = getOrderById(orderId);
        orderValidator.isOrderActive(order);
        User executorOrder = activeUserService.getActiveUser();
        User ownerOrder = userService.getUserById(order.getUser().getId());
        Account ownerBasicAccount = accountService.getAccountByUserIdAndCurrency(ownerOrder.getId(), currencyService.getBasicCurrency());
        Account ownerOrderAccount = accountService.getAccountByUserIdAndCurrency(ownerOrder.getId(), order.getCurrency().getCode());

        Account executerBasicAccount = accountService.getAccountByUserIdAndCurrency(executorOrder.getId(), currencyService.getBasicCurrency());
        Account executerOrderAccount = accountService.getAccountByUserIdAndCurrency(executorOrder.getId(), order.getCurrency().getCode());
        OperationType operationType = order.getType();

        BigDecimal transferAmount = calculateTransferAmount(order, executerOrderAccount, executerBasicAccount);
        BigDecimal orderFee = transferAmount.multiply(fee).multiply(order.getRateValue());
        OrderTransferDTO buyOrderTransferDTO = new OrderTransferDTO(order, ownerOrder, executorOrder, ownerBasicAccount, ownerOrderAccount, executerBasicAccount, executerOrderAccount, transferAmount);
        OrderTransferDTO sellOrderTransferDTO = new OrderTransferDTO(order, executorOrder, ownerOrder, executerBasicAccount, executerOrderAccount, ownerBasicAccount, ownerOrderAccount, transferAmount);


        if (operationType.equals(OperationType.BUY)){
            accountBusinessService.reserveMoney(executerOrderAccount, transferAmount);
            orderTransferOperation(buyOrderTransferDTO);
            payOrderFee(ownerBasicAccount, executerBasicAccount, orderFee, order.getType());
        }
        else {
            accountBusinessService.reserveMoney(executerBasicAccount, (transferAmount.multiply(order.getRateValue()).add(orderFee)));
            orderTransferOperation(sellOrderTransferDTO);
            payOrderFee(ownerBasicAccount, executerBasicAccount, orderFee, order.getType());
        }
        order.setOrderFee(order.getAmount().multiply(fee).multiply(order.getRateValue()));
        orderRepository.save(order);
    }

    private void payOrderFee(Account owner, Account executor, BigDecimal amountFee, OperationType type){
        User manager = managerUserService.getManager();
        Account managerBasicAccount = accountService.getAccountByUserIdAndCurrency(manager.getId(), currencyService.getBasicCurrency());

        if (type.equals(OperationType.BUY)){
            owner.setOrderBalance(owner.getOrderBalance().subtract(amountFee));
            managerBasicAccount.setBalance(managerBasicAccount.getBalance().add(amountFee));
            executor.setBalance(executor.getBalance().subtract(amountFee));
            managerBasicAccount.setBalance(managerBasicAccount.getBalance().add(amountFee));
        }
        else {
            owner.setBalance(owner.getBalance().subtract(amountFee));
            managerBasicAccount.setBalance(managerBasicAccount.getBalance().add(amountFee));
            executor.setOrderBalance(executor.getOrderBalance().subtract(amountFee));
            managerBasicAccount.setBalance(managerBasicAccount.getBalance().add(amountFee));
        }



    }
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        orderValidator.isOrderActive(order);
        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("CANCELLED");
        Account orderAccount = accountBusinessService.getAccountFromOrder(order);

        BigDecimal orderAmount = getOrderAmount(order);
        System.out.println(orderAmount);
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
        return order.getType().equals(OperationType.BUY) ? (order.getAmount().add(order.getAmount().multiply(fee))).multiply(order.getRateValue()) : order.getAmount();
    }

    @Override
    public void cancelAllOrders(){

        orderRepository.findAll().stream()
                        .filter(Objects::nonNull)
                                .filter(order -> order.getStatus().equals(OrderStatus.ACTIVE))
                                        .forEach(order -> cancelOrder(order.getId()));

        //orderRepository.findAll().forEach(order -> cancelOrder(order.getId()));
    }
}
