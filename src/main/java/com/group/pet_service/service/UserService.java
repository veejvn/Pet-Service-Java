package com.group.pet_service.service;

import com.group.pet_service.dto.request.UserCreationRequest;
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
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Transactional
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
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "User not found", "user-e-01")
        );
        userRepository.delete(user);
    }

    public UserResponse updateToStaff(UserUpgradeToStaffRequest request) {
        User user = userRepository.findById(request.getId()).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "User not found", "user-e-01")
        );
        user.getRoles().add(Role.STAFF);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void addStaff(UserCreationRequest request) {
        boolean existedStaff = userRepository.existsByEmail(request.getEmail());
        if (existedStaff) {
            throw new AppException(HttpStatus.NOT_FOUND, "Staff existed", "user-e-02");
        }

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        String verifyCode = helper.generateTempPwd(6);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(role.name()).ifPresent(roles::add);

        user.setRoles(roles);
        user.setVerificationCode(verifyCode);
        user.setVerificationExpiry(LocalDateTime.now().plusHours(24));//thoi gian het han code 24h
        user.setCreatedAt(new Date());
        user.setVerified(false);

        user = userRepository.save(user);

        //Send Verifier Email
        Map<String, Object> props = new HashMap<>();
        props.put("firstName", request.getFirstname());
        props.put("lastName", request.getLastname());
        props.put("code", verifyCode);

        DataMailDTO dataMailDTO = DataMailDTO.builder()
                .subject("XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG")
                .to(request.getEmail())
                .props(props)
                .build();
        mailService.sendHtmlMail(dataMailDTO);


        return userMapper.toUserResponse(user);
    }

//    @Transactional
//    public void addStaffImg(String staffId, MultipartFile[] images) throws IOException {
//        // Find the PetService by ID or throw an exception if not found
//        User user = userRepository.findById(staffId)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
////        testCloudinary();
//        Set<UserImage> userImages = new HashSet<>();
//
//        for (MultipartFile image : images) {
//            try {
//                // Upload image to Cloudinary
//                Map<String, Object> uploadResult = cloudinary.uploader()
//                        .upload(image.getBytes(), ObjectUtils.asMap("folder", "pet_service_images/"));
//
//                // Extract the secure URL of the uploaded image
//                String url = (String) uploadResult.get("secure_url");
//
//                // Create a ServiceImage entity and associate it with the PetService
//                UserImage userImage = UserImage.builder()
//                        .url(url)
//                        .user(user)
//                        .build();
//
//                userImages.add(userImage);
//            } catch (Exception e) {
//                throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
//            }
//        }
//
//        userImageRepository.saveAll(userImages);
//    }

//    @Transactional
//    public UserResponse updateStaff(UserEditRequest request) {
//        User existingUser = userRepository.findById(request.getId())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
//
//
//        existingUser.setFirstname(request.getFirstname());
//        existingUser.setLastname(request.getLastname());
//        existingUser.setGender(request.isGender());
//        existingUser.setDob(request.getDob());
//        existingUser.setPhoneNum(request.getPhoneNum());
//        existingUser.setPassword(request.getPassword());
//        existingUser.setEmail(request.getEmail());
//
//        userRepository.save(existingUser);
//
//        return userMapper.toUserResponse(existingUser);
//    }

}
