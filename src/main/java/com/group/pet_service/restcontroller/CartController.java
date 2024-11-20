package com.group.pet_service.restcontroller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group.pet_service.dto.CartItemDTO;
import com.group.pet_service.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	// Add a service to cart
	@PostMapping("/add")
	public ResponseEntity<CartItemDTO> addToCart(@Valid @RequestBody CartItemDTO cartItemDTO, Principal principal) {
		String userId = principal.getName();
		CartItemDTO addedItem = cartService.addToCart(userId, cartItemDTO);
		return ResponseEntity.ok(addedItem);
	}

	// Update cart item quantity
	@PutMapping("/update/{cartItemId}")
	public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable Long cartItemId, @RequestParam int quantity,
			Principal principal) {
		String userId = principal.getName();
		CartItemDTO updatedItem = cartService.updateCartItemQuantity(userId, cartItemId, quantity);
		return ResponseEntity.ok(updatedItem);
	}

	// Remove a specific cart item
	@DeleteMapping("/remove/{cartItemId}")
	public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId, Principal principal) {
		String userId = principal.getName(); // Lấy userId từ Principal (người dùng hiện tại)
		cartService.removeCartItem(userId, cartItemId); // Gọi phương thức service để xóa
		return ResponseEntity.noContent().build(); // Trả về 204 No Content nếu thành công
	}

	// Get all cart items for the current user
	@GetMapping
	public ResponseEntity<List<CartItemDTO>> getUserCart(Principal principal) {
		// Lấy userId từ Principal (người dùng hiện tại)
		String userId = principal.getName();

		// Gọi phương thức service để lấy các mục giỏ hàng của người dùng
		List<CartItemDTO> cartItems = cartService.getUserCartItems(userId);

		// Trả về danh sách CartItemDTO dưới dạng ResponseEntity
		return ResponseEntity.ok(cartItems);
	}

	// Clear entire cart
	@DeleteMapping("/clear")
	public ResponseEntity<Void> clearCart(Principal principal) {
		String userId = principal.getName(); // Lấy userId từ Principal (người dùng hiện tại)
		cartService.clearCart(userId); // Xoá tất cả mục trong giỏ hàng của người dùng
		return ResponseEntity.ok().build(); // Trả về mã trạng thái 200 OK
	}

}