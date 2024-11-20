package com.group.pet_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.group.pet_service.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	Optional<CartItem> findByIdAndUserId(Long cartItemId, String userId);
	

	 // Xóa CartItem dựa trên userId và cartItemId
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.id = :cartItemId AND c.user.id = :userId")
    void removeCartItem(String userId, Long cartItemId);


	List<CartItem> findByUserId(String userId);

//	CartItemDTO updateCartItemQuantity(String userId, Long cartItemId, int quantity);
//
//	void removeCartItem(String userId, Long cartItemId);
//
//	List<CartItemDTO> getUserCartItems(String userId);
//
//	@Modifying
//	@Transactional
//	@Query("DELETE FROM CartItem c WHERE c.user.id = :userId")
//	void clearCart(@Param("userId") String userId);
}