package com.group.pet_service.mapper;

import com.group.pet_service.dto.pet_service_item.PetServiceItemRequest;
import org.mapstruct.Mapper;
import com.group.pet_service.model.PetServiceItem;

@Mapper(componentModel = "spring")
public interface ServiceItemMapper {
    PetServiceItemRequest toDTO(PetServiceItem petServiceItem);

    PetServiceItem toEntity(PetServiceItemRequest petServiceItemRequest);
}