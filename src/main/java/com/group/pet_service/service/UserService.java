package com.group.pet_service.service;

import com.group.pet_service.dto.request.UserUpdateRequest;
import com.group.pet_service.dto.request.UserUpgradeToStaffRequest;
import com.group.pet_service.dto.response.UserResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.UserMapper;
import com.group.pet_service.enums.Role;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.util.PasswordUtil;
import com.group.pet_service.util.UserUtil;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordUtil passwordUtil;
    UserUtil userUtil;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponse> getUsers(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(){
        User user = userUtil.getUser();
        return userMapper.toUserResponse(user);
    }

    public UserResponse  updateInfo(UserUpdateRequest request){
        String userId = userUtil.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(
                        HttpStatus.BAD_REQUEST, "User not exist", "user-e-01"
                ));
        userMapper.updateUserInfo(user, request);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    public UserResponse updateToStaff(UserUpgradeToStaffRequest request){
        User user = userRepository.findById(request.getId()).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "User not found", "user-e-01")
        );
        user.getRoles().add(Role.STAFF);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
