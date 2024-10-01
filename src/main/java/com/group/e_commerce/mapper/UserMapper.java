package com.group.e_commerce.mapper;

import com.group.e_commerce.dto.request.UserCreationRequest;
import com.group.e_commerce.dto.request.UserUpdateRequest;
import com.group.e_commerce.dto.response.UserResponse;
import com.group.e_commerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);


    //void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
