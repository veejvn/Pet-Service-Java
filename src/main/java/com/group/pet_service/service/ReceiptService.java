package com.group.pet_service.service;

import com.group.pet_service.dto.request.ReceiptCreateRequest;
import com.group.pet_service.dto.response.ReceiptResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.ReceiptMapper;
import com.group.pet_service.model.Pet;
import com.group.pet_service.model.Receipt;
import com.group.pet_service.model.ServiceItem;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.PetRepository;
import com.group.pet_service.repository.ReceiptRepository;
import com.group.pet_service.repository.ServiceRepository;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final UserUtil userUtil;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ReceiptMapper receiptMapper;

    public ReceiptResponse create(ReceiptCreateRequest request){
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
        Set<ServiceItem> items = new HashSet<>();
        for(ReceiptCreateRequest.ServiceItemDTO serviceItemDTO : request.getItems()){
            User staff = userRepository.findById(serviceItemDTO.getStaffId()).orElseThrow(
                    () -> new AppException(HttpStatus.NOT_FOUND, "Staff not found", "user-e-03")
            );
            com.group.pet_service.model.Service service = serviceRepository.findById(serviceItemDTO.getServiceId()).orElseThrow(
                    () -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "service-e-01")
            );
            int quantity = serviceItemDTO.getQuantity();
            totalItem += quantity;
            double totalPrice = service.getPrice() * quantity;
            totalPriceReceipt += totalPrice;
            Timestamp start = serviceItemDTO.getStart();
            Timestamp end = serviceItemDTO.getEnd();
            ServiceItem serviceItem = ServiceItem.builder()
                    .quantity(quantity)
                    .totalPrice(totalPrice)
                    .start(start)
                    .end(end)
                    .service(service)
                    .receipt(receipt)
                    .staff(staff)
                    .build();
            items.add(serviceItem);
        }
        receipt.setItems(items);
        receipt.setTotalItem(totalItem);
        receipt.setTotalPriceReceipt(totalPriceReceipt);
        receiptRepository.save(receipt);
        return receiptMapper.toReceiptResponse(receipt);
    }

}
