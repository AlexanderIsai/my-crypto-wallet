package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.dto.OrderAddDTO;

/**
 * description
 *
 * @author Alexander Isai on 25.01.2024.
 */
public interface OrderService {

    void addOrder(OrderAddDTO orderDTO);
}
