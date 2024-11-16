package com.group.pet_service.dto.response;

import com.group.pet_service.enums.ServiceItemStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceiptResponse {
    String id;
    int totalItem;
    double totalPriceReceipt;
    Timestamp createdAt;
    Set<ServiceItemDTO> items;
    UserResponse user;
    PetResponse pet;

    @Data
    public static class ServiceItemDTO {
        ServiceItemDTO id;
        ServiceItemStatus status;
        int quantity;
        double totalPrice;
        Timestamp start;
        Timestamp end;
        UserResponse staff;
    }
}
