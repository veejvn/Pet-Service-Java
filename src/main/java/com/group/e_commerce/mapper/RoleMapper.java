package com.group.e_commerce.mapper;

import com.group.e_commerce.dto.request.RoleRequest;
import com.group.e_commerce.dto.response.RoleResponse;
import com.group.e_commerce.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);

}
