package com.group.pet_service.mapper;

import com.group.pet_service.dto.request.RoleRequest;
import com.group.pet_service.dto.response.RoleResponse;
import com.group.pet_service.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);

}
