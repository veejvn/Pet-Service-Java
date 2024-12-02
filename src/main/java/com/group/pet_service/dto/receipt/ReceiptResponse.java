package com.group.pet_service.dto.receipt;

import com.group.pet_service.dto.staff.StaffResponse;
import com.group.pet_service.dto.pet.PetResponse;
import com.group.pet_service.dto.pet_service.PetServiceResponse;
import com.group.pet_service.dto.user.UserResponse;
import com.group.pet_service.enums.PetServiceItemStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceiptResponse {
    String id;
    Integer totalItem;
    Integer totalPriceReceipt;
    LocalDateTime createdAt;
    Set<PetServiceItemDTO> items;
    UserResponse user;
    PetResponse pet;

    @Data
    public static class PetServiceItemDTO {
        PetServiceItemStatus status;
        LocalDateTime start;
        LocalDateTime end;
        StaffResponse staff;
        PetServiceResponse petService;
    }
}
