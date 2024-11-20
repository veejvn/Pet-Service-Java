package com.group.pet_service.mapper;

import com.group.pet_service.dto.request.UserCreationRequest;
import com.group.pet_service.dto.request.UserEditRequest;
import com.group.pet_service.dto.response.UserResponse;
import com.group.pet_service.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    UserEditRequest toUserEditRequest(User user);

    //void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
