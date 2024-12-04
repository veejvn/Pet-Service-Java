package com.group.pet_service.service;

import com.group.pet_service.dto.pet_service_item.ChangeStatusPetServiceItemRequest;
import com.group.pet_service.dto.pet_service_item.PetServiceItemResponse;
import com.group.pet_service.dto.receipt.ReceiptCreateRequest;
import com.group.pet_service.dto.receipt.ReceiptResponse;
import com.group.pet_service.enums.PetServiceItemStatus;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.PetServiceItemMapper;
import com.group.pet_service.mapper.ReceiptMapper;
import com.group.pet_service.model.*;
import com.group.pet_service.model.PetService;
import com.group.pet_service.repository.*;
import com.group.pet_service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final UserUtil userUtil;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetServiceRepository petServiceRepository;
    private final PetServiceItemRepository petServiceItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ReceiptMapper receiptMapper;
    private final PetServiceItemMapper petServiceItemMapper;

    public ReceiptResponse create(ReceiptCreateRequest request) {
        User user = userUtil.getUser();
        Pet pet = petRepository.findById(request.getPetId()).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet not found", "pet-e-01")
        );
        Receipt receipt = Receipt.builder()
                .createdAt(LocalDateTime.now())
                .user(user)
                .pet(pet)
                .build();
//        receiptRepository.save(receipt);
        int totalItem = 0;
        int totalPriceReceipt = 0;
        Set<PetServiceItem> items = new HashSet<>();
        for (ReceiptCreateRequest.PetServiceItemDTO PetServiceItemDTO : request.getItems()) {
            User staff = userRepository.findById(PetServiceItemDTO.getStaffId()).orElseThrow(
                    () -> new AppException(HttpStatus.NOT_FOUND, "Staff not found", "user-e-03")
            );
            CartItem cartItem = cartItemRepository.findById(PetServiceItemDTO.getCartItemId()).orElseThrow(
                    () -> new AppException(HttpStatus.NOT_FOUND, "Cart item not found", "cart-item-e-01")
            );
            PetService petService = cartItem.getPetService();
            totalItem += 1;
            totalPriceReceipt += petService.getPrice();
            LocalDateTime start = PetServiceItemDTO.getStart();
            LocalDateTime end = PetServiceItemDTO.getEnd();
            PetServiceItem petServiceItem = PetServiceItem.builder()
                    .start(start)
                    .end(end)
                    .petService(petService)
                    .receipt(receipt)
                    .staff(staff)
                    .build();
            items.add(petServiceItem);
            cartItemRepository.delete(cartItem);
        }
        receipt.setItems(items);
        receipt.setTotalItem(totalItem);
        receipt.setTotalPriceReceipt(totalPriceReceipt);
        receiptRepository.save(receipt);
        return receiptMapper.toReceiptResponse(receipt);
    }

    public List<ReceiptResponse> getAll() {
        String id = userUtil.getUserId();
        List<Receipt> receipts = receiptRepository.findAllByUserId(id);
        return receiptMapper.toListReceiptResponse(receipts);
    }

    public List<ReceiptResponse> getReceiptByStaff() {
        String id = userUtil.getUserId();
        List<Receipt> receipts = receiptRepository.findDistinctByItems_Staff_Id(id);
        return receiptMapper.toListReceiptResponse(receipts);
    }

    public PetServiceItemResponse staffChangeStatusPetServiceItem(String id, ChangeStatusPetServiceItemRequest request) {
        String staffId = userUtil.getUserId();
        PetServiceItem petServiceItem = petServiceItemRepository.findByIdAndStaffId(id, staffId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet service item not found", "pet-service-item-e-01")
        );

        PetServiceItemStatus status = request.getStatus();

        Set<PetServiceItemStatus> statuses = Set.of(
                PetServiceItemStatus.CONFIRMED,
                PetServiceItemStatus.COMPLETED,
                PetServiceItemStatus.CANCELED
        );

        if (!statuses.contains(status)) {
            throw new AppException(HttpStatus.FORBIDDEN, "You don't have permission to edit pet service item to this status.", "pet-service-item-e-02");
        }

        petServiceItem.setStatus(status);
        petServiceItemRepository.save(petServiceItem);
        return petServiceItemMapper.toPetServiceItemResponse(petServiceItem);
    }

    public PetServiceItemResponse userChangeStatusPetServiceItem(String id) {
        PetServiceItem petServiceItem = petServiceItemRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet service item not found", "pet-service-item-e-01")
        );
        petServiceItem.setStatus(PetServiceItemStatus.CANCELED);
        petServiceItemRepository.save(petServiceItem);
        return petServiceItemMapper.toPetServiceItemResponse(petServiceItem);
    }

    public Page<ReceiptResponse> findAll(Pageable pageable) {
        Page<Receipt> receipts = receiptRepository.findAllByOrderByCreatedAtDesc(pageable);
        return receiptMapper.toPetServiceResponsePage(receipts);
    }

    public ReceiptResponse findById(String id) {
        Receipt receipt = receiptRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Receipt not found")
        );
        return receiptMapper.toReceiptResponse(receipt);
    }
}
