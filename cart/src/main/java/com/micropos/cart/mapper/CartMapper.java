package com.micropos.cart.mapper;

import com.micropos.cart.dto.CounterDto;
import com.micropos.cart.models.Cart;
import com.micropos.cart.dto.CartDto;
import com.micropos.cart.models.Counter;
import org.mapstruct.Mapper;

@Mapper
public interface CartMapper {
    Cart toCart(CartDto cartDto);

    CartDto toCartDto(Cart cart);

    CounterDto toCounterDto(Counter counter);

    Counter toCounter(CounterDto counterDto);
}
