package com.group.pet_service.dto.pet_service_item;

import com.group.pet_service.dto.pet_service.PetServiceResponse;
import com.group.pet_service.dto.staff.StaffResponse;
import com.group.pet_service.enums.PetServiceItemStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PetServiceItemResponse {
    String id;
    LocalDateTime start;
    LocalDateTime end;
    PetServiceItemStatus status;
    PetServiceResponse petService;
    StaffResponse staff;
}
