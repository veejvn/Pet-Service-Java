package com.group.pet_service.service;

import com.group.pet_service.dto.staff.StaffEditRequest;
import com.group.pet_service.dto.staff.StaffResponse;
import com.group.pet_service.dto.staff.StaffCreationRequest;
import com.group.pet_service.dto.user.UserUpdateRequest;
import com.group.pet_service.dto.user.UserResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.UserMapper;
import com.group.pet_service.enums.Role;
import com.group.pet_service.model.JobPosition;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.JobPositionRepository;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.util.PasswordUtil;
import com.group.pet_service.util.UserUtil;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Transactional
@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    JobPositionRepository jobPositionRepository;
    UploadService uploadService;
    UserMapper userMapper;
    PasswordUtil passwordUtil;
    UserUtil userUtil;

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser() {
        User user = userUtil.getUser();
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateInfo(UserUpdateRequest request) {
        String userId = userUtil.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(
                        HttpStatus.BAD_REQUEST, "User not exist", "user-e-01"
                ));
        userMapper.updateUserInfo(user, request);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void delete(String id) {
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new AppException("Cannot delete species: It is being referenced by other records.");
        } catch (Exception e) {
            throw new AppException("An unexpected error occurred while deleting species.");
        }
    }

    public void addStaff(StaffCreationRequest request) throws IOException {
        boolean existedStaff = userRepository.existsByEmail(request.getEmail());
        if (existedStaff) {
            throw new AppException(HttpStatus.NOT_FOUND, "Email existed");
        }

        JobPosition jobPosition = jobPositionRepository.findById(request.getJobPositionId()).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Job position not found")
        );

        User user = userMapper.toUser(request);

        String avatar = uploadService.uploadFile(request.getAvatar());
        user.setPassword(passwordUtil.encodePassword(request.getPassword()));
        Set<Role> roles = Set.of(
                Role.USER,
                Role.STAFF
        );
        user.setRoles(roles);
        user.setAvatar(avatar);
        user.setJobPosition(jobPosition);
        userRepository.save(user);

    }

    public StaffEditRequest findById(String id) {
        User staff = userRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Staff not found")
        );
        return userMapper.toStaffEditRequest(staff);
    }

    public List<StaffResponse> findAllStaffs() {
        List<User> allUsers = userRepository.findAllByOrderByCreatedAtDesc();
        List<User> staffUsers = allUsers.stream()
                .filter(user -> user.getRoles().contains(Role.STAFF))
                .filter(user -> !"admin".equalsIgnoreCase(user.getDisplayName()))
                .toList();
        return userMapper.toListStaffResponse(staffUsers);
    }

    public void updateStaff(String id, StaffEditRequest request) throws IOException {
        JobPosition jobPosition = jobPositionRepository.findById(request.getJobPositionId()).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Job position not found")
        );
        User staff = userRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Staff not found")
        );
        userMapper.updateStaffInfo(staff, request);
        if (request.getAvatarFile() != null && !request.getAvatarFile().isEmpty()) {
            String avatar = uploadService.uploadFile(request.getAvatarFile());
            staff.setAvatar(avatar);
        }
        staff.setJobPosition(jobPosition);
        userRepository.save(staff);
    }
}
