package de.telran.mycryptowallet.service.utils.validators;
import de.telran.mycryptowallet.entity.Order;
import de.telran.mycryptowallet.entity.entityEnum.OrderStatus;
import de.telran.mycryptowallet.exceptions.NotActiveOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author Alexander Isai on 07.02.2024.
 */
@Component
@RequiredArgsConstructor
public class OrderValidator {

    public void isOrderActive(Order order){
        if (!order.getStatus().equals(OrderStatus.ACTIVE)){
            throw new NotActiveOrderException("This order is not active");
        }
    }
}
