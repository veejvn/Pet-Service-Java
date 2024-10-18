package com.group.pet_service.mapper;

import org.mapstruct.Mapper;

import com.group.pet_service.dto.request.RoleRequest;
import com.group.pet_service.dto.response.RoleResponse;
import com.group.pet_service.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);

}
