package com.group.pet_service.service;

import com.group.pet_service.dto.cart.CartItemCreationRequest;
import com.group.pet_service.dto.cart.CartItemResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.CartItemMapper;
import com.group.pet_service.model.CartItem;
import com.group.pet_service.model.PetService;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.CartItemRepository;
import com.group.pet_service.repository.PetServiceRepository;
import com.group.pet_service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final PetServiceRepository petServiceRepository;
    private final CartItemMapper cartItemMapper;
    private final UserUtil userUtil;

    public CartItemResponse addCartItem(CartItemCreationRequest request) {
        PetService petService = petServiceRepository.findById(request.getPetServiceId()).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet service not found", "pet-service-e-01")
        );
        boolean hasExistedPetService = cartItemRepository.existsByPetServiceId(request.getPetServiceId());
        if (hasExistedPetService) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Pet service already in cart", "cart-item-e-01");
        }
        User user = userUtil.getUser();
        CartItem cartItem = CartItem.builder()
                .petService(petService)
                .user(user)
                .build();
        cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponse(cartItem);
    }

    public List<CartItemResponse> getAll() {
        String userId = userUtil.getUserId();
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItemMapper.toListCartItemResponse(cartItems);
    }

    public void delete(String id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Cart item not found", "cart-item-e-02")
        );
        cartItemRepository.delete(cartItem);
    }
}
