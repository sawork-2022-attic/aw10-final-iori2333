package com.micropos.cart.mapper;

import com.micropos.cart.models.Cart;
import com.micropos.cart.dto.CartDto;
import org.mapstruct.Mapper;

@Mapper
public interface CartMapper {
    Cart toCart(CartDto cartDto);

    CartDto toCartDto(Cart cart);
}
