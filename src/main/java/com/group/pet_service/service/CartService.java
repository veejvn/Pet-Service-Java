package com.group.pet_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group.pet_service.dto.CartItemDTO;
import com.group.pet_service.model.CartItem;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.CartItemRepository;
import com.group.pet_service.repository.ServiceRepository;
import com.group.pet_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartService {
	private final CartItemRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository; // Dùng để lưu CartItem
	@Autowired
	private ServiceRepository serviceRepository; // Dùng để lấy thông tin Service
	@Autowired
	private UserRepository userRepository; // Dùng để lấy thông tin User

	public CartItemDTO addToCart(String userId, CartItemDTO cartItemDTO) {
		// 1. Lấy người dùng từ userId
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// 2. Lấy dịch vụ từ serviceId
		com.group.pet_service.model.Service service = serviceRepository.findById(cartItemDTO.getServiceId())
				.orElseThrow(() -> new RuntimeException("Service not found"));

		// 3. Tạo một CartItem mới
		CartItem cartItem = new CartItem();
		cartItem.setQuantity(cartItemDTO.getQuantity());
		cartItem.setService(service);
		cartItem.setUser(user);

		// 4. Lưu CartItem vào cơ sở dữ liệu
		CartItem savedCartItem = cartItemRepository.save(cartItem);

		// 5. Chuyển đổi CartItem sang CartItemDTO để trả về
		CartItemDTO resultDTO = new CartItemDTO();
		resultDTO.setId(savedCartItem.getId());
		resultDTO.setServiceId(savedCartItem.getService().getId());
		resultDTO.setQuantity(savedCartItem.getQuantity());

		return resultDTO;
	}

	public CartItemDTO updateCartItemQuantity(String userId, Long cartItemId, int quantity) {
		// Kiểm tra số lượng hợp lệ
		if (quantity < 1) {
			throw new IllegalArgumentException("Quantity must be at least 1.");
		}

		// Tìm mục giỏ hàng dựa trên userId và cartItemId
		CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
				.orElseThrow(() -> new RuntimeException("Cart item not found."));

		// Cập nhật số lượng mới
		cartItem.setQuantity(quantity);
		cartItem = cartItemRepository.save(cartItem);

		// Chuyển đổi CartItem thành DTO
		CartItemDTO cartItemDTO = new CartItemDTO();
		cartItemDTO.setId(cartItem.getId());
		cartItemDTO.setQuantity(cartItem.getQuantity());
		cartItemDTO.setServiceId(cartItem.getService().getId());

		return cartItemDTO;
	}

	public void removeCartItem(String userId, Long cartItemId) {
		// Kiểm tra xem CartItem có tồn tại không
		CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
				.orElseThrow(() -> new RuntimeException("Cart item not found."));

		// Xóa mục giỏ hàng
		cartItemRepository.removeCartItem(userId, cartItemId);
	}

	public List<CartItemDTO> getUserCartItems(String userId) {
		// Lấy tất cả CartItems của người dùng từ repository
		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

		// Chuyển đổi danh sách CartItems thành CartItemDTOs
		List<CartItemDTO> cartItemDTOs = cartItems.stream().map(
				cartItem -> new CartItemDTO(cartItem.getId(), cartItem.getService().getId(), cartItem.getQuantity()))
				.collect(Collectors.toList());

		return cartItemDTOs;
	}

	public void clearCart(String userId) {
		// Tìm tất cả các CartItem của người dùng
		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

		// Xoá tất cả các CartItem
		cartItemRepository.deleteAll(cartItems);
	}

//
//	public List<CartItemDTO> getUserCartItems(String userId) {
//		// TODO Auto-generated method stub
//		return cartRepository.getUserCartItems(userId);
//	}
//
//	public void clearCart(String userId) {
//		// TODO Auto-generated method stub
//		cartRepository.clearCart(userId);
//	}
//   
}