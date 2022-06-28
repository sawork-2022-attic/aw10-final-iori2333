package com.micropos.order.mapper;

import com.micropos.order.dto.OrderDto;
import com.micropos.order.models.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {
    Order toOrder(OrderDto orderDto);
    OrderDto toOrderDto(Order order);
}
