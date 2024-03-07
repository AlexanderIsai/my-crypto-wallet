package de.telran.mycryptowallet.mapper.orderMapper;

import de.telran.mycryptowallet.dto.orderDTO.OrderAddDTO;
import de.telran.mycryptowallet.dto.orderDTO.OrderOutDTO;
import de.telran.mycryptowallet.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 04.03.2024.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "currency.code", target = "code")
    OrderOutDTO toDto(Order order);

    @Mapping(source = "orderRate", target = "rateValue")
    Order toEntity (OrderAddDTO orderAddDTO);

    List<OrderOutDTO> toDtoList(List<Order> orders);
}
