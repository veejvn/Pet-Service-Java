package com.group.pet_service.dto.receipt;

import com.group.pet_service.dto.pet.PetResponse;
import com.group.pet_service.dto.pet_service.PetServiceResponse;
import com.group.pet_service.dto.user.UserResponse;
import com.group.pet_service.enums.ServiceItemStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceiptResponse {
    String id;
    Integer totalItem;
    Double totalPriceReceipt;
    Timestamp createdAt;
    Set<ServiceItemDTO> items;
    UserResponse user;
    PetResponse pet;

    @Data
    public static class ServiceItemDTO {
        String id;
        ServiceItemStatus status;
        int quantity;
        double totalPrice;
        Timestamp start;
        Timestamp end;
        UserResponse staff;
        PetServiceResponse service;
    }
}
