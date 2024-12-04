package com.group.pet_service.mapper;

import com.group.pet_service.dto.cart.CartItemResponse;
import com.group.pet_service.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toListCartItemResponse(List<CartItem> cartItems);
}
