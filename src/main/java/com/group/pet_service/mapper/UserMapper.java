package com.group.pet_service.mapper;

import com.group.pet_service.dto.auth.UserInfoResponse;
import com.group.pet_service.dto.jwt.JWTPayloadDto;
import com.group.pet_service.dto.request.UserCreationRequest;
import com.group.pet_service.dto.request.UserUpdateRequest;
import com.group.pet_service.dto.request.UserEditRequest;
import com.group.pet_service.dto.response.UserResponse;
import com.group.pet_service.enums.Role;
import com.group.pet_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.StringJoiner;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    UserInfoResponse toUserInfoResponse(User user);
    void updateUserInfo(@MappingTarget User user, UserUpdateRequest request);

    @Mapping(source = "roles", target = "scope", qualifiedByName = "buildScope")
    JWTPayloadDto toJWTPayloadDto(User user);

    @Named("buildScope")
    static String buildScope(Set<Role> roles){
        StringBuilder scopeBuilder = new StringBuilder();
        for (Role role : roles) {
            scopeBuilder.append(String.format("ROLE_%s ", role.name()));
        }
        return scopeBuilder.toString().trim();
    }
    UserEditRequest toUserEditRequest(User user);

    //void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
