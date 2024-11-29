package com.group.pet_service.service;

import com.group.pet_service.dto.receipt.ReceiptCreateRequest;
import com.group.pet_service.dto.receipt.ReceiptResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.ReceiptMapper;
import com.group.pet_service.model.Pet;
import com.group.pet_service.model.PetService;
import com.group.pet_service.model.Receipt;
import com.group.pet_service.model.PetServiceItem;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.PetRepository;
import com.group.pet_service.repository.ReceiptRepository;
import com.group.pet_service.repository.PetServiceRepository;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    private final ReceiptMapper receiptMapper;

    public ReceiptResponse create(ReceiptCreateRequest request) {
        User user = userUtil.getUser();
        Pet pet = petRepository.findById(request.getPetId()).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet not found", "pet-e-01")
        );
        Receipt receipt = Receipt.builder()
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .user(user)
                .pet(pet)
                .build();
//        receiptRepository.save(receipt);
        int totalItem = 0;
        double totalPriceReceipt = 0.0;
        Set<PetServiceItem> items = new HashSet<>();
        for (ReceiptCreateRequest.PetServiceItemDTO serviceItemDTO : request.getItems()) {
            User staff = userRepository.findById(serviceItemDTO.getStaffId()).orElseThrow(
                    () -> new AppException(HttpStatus.NOT_FOUND, "Staff not found", "user-e-03")
            );
            PetService petService = petServiceRepository.findById(serviceItemDTO.getServiceId()).orElseThrow(
                    () -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "service-e-01")
            );
            totalItem += 1;
            totalPriceReceipt += petService.getPrice();
            Timestamp start = serviceItemDTO.getStart();
            Timestamp end = serviceItemDTO.getEnd();
            PetServiceItem petServiceItem = PetServiceItem.builder()
                    .start(start)
                    .end(end)
                    .petService(petService)
                    .receipt(receipt)
                    .staff(staff)
                    .build();
            items.add(petServiceItem);
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

    public Page<ReceiptResponse> findAll(Pageable pageable) {
        Page<Receipt> receipts = receiptRepository.findAll(pageable);
        return receiptMapper.toPetServiceResponsePage(receipts);
    }
}
