package com.group.pet_service.mapper;

import com.group.pet_service.dto.staff.StaffEditRequest;
import com.group.pet_service.dto.staff.StaffResponse;
import com.group.pet_service.dto.auth.UserInfoResponse;
import com.group.pet_service.dto.jwt.JWTPayloadDto;
import com.group.pet_service.dto.staff.StaffCreationRequest;
import com.group.pet_service.dto.user.UserUpdateRequest;
import com.group.pet_service.dto.user.UserResponse;
import com.group.pet_service.enums.Role;
import com.group.pet_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "avatar", ignore = true)
    User toUser(StaffCreationRequest request);

    @Mapping(source = "user.jobPosition.name", target = "jobPosition")
    UserResponse toUserResponse(User user);

    UserInfoResponse toUserInfoResponse(User user);

    void updateUserInfo(@MappingTarget User user, UserUpdateRequest request);

    @Mapping(source = "roles", target = "scope", qualifiedByName = "buildScope")
    JWTPayloadDto toJWTPayloadDto(User user);

    @Named("buildScope")
    static String buildScope(Set<Role> roles) {
        StringBuilder scopeBuilder = new StringBuilder();
        for (Role role : roles) {
            scopeBuilder.append(String.format("ROLE_%s ", role.name()));
        }
        return scopeBuilder.toString().trim();
    }

    @Mapping(source = "user.jobPosition.name", target = "jobPosition")
    StaffResponse toStaffResponse(User user);

    List<StaffResponse> toListStaffResponse(List<User> staffs);

    @Mapping(source = "jobPosition.id", target = "jobPositionId")
    StaffEditRequest toStaffEditRequest(User user);

    @Mapping(target = "avatar", ignore = true)
    void updateStaffInfo(@MappingTarget User staff, StaffEditRequest request);
}
