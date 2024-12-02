package com.group.pet_service.mapper;

import com.group.pet_service.dto.pet_service_item.PetServiceItemResponse;
import com.group.pet_service.dto.staff.StaffResponse;
import com.group.pet_service.model.PetServiceItem;
import com.group.pet_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetServiceItemMapper {

    @Mapping(source = "jobPosition.name", target = "jobPosition")
    StaffResponse toStaffResponse(User user);

    PetServiceItemResponse toPetServiceItemResponse(PetServiceItem petServiceItem);
}
