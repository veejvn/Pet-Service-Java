package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.api.ApiResponse;
import com.group.pet_service.dto.cart.CartItemCreationRequest;
import com.group.pet_service.dto.cart.CartItemResponse;
import com.group.pet_service.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart-items")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<ApiResponse<CartItemResponse>> create(@RequestBody @Valid CartItemCreationRequest request) {
        ApiResponse<CartItemResponse> apiResponse = ApiResponse.<CartItemResponse>builder()
                .code("cart-item-s-1")
                .message("Add cart item successfully")
                .data(cartItemService.addCartItem(request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getAll() {
        ApiResponse<List<CartItemResponse>> apiResponse = ApiResponse.<List<CartItemResponse>>builder()
                .code("cart-item-s-2")
                .message("Get cart items successfully")
                .data(cartItemService.getAll())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") String id) {
        cartItemService.delete(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("cart-item-s-3")
                .message("Delete cart item successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
