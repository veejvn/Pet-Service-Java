package com.group.pet_service.dto.cart;

import com.group.pet_service.dto.pet_service.PetServiceResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    String id;
    PetServiceResponse petService;

}
