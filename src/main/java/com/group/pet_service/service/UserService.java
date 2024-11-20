package com.group.pet_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.group.pet_service.constant.PredefinedRole;
import com.group.pet_service.dto.DataMailDTO;
import com.group.pet_service.dto.request.ResendCodeRequest;
import com.group.pet_service.dto.request.UserCreationRequest;
import com.group.pet_service.dto.request.UserUpdateRequest;
import com.group.pet_service.dto.request.UserVerifyCodeRequest;
import com.group.pet_service.dto.response.UserResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.exception.ErrorCode;
import com.group.pet_service.mapper.UserMapper;
import com.group.pet_service.model.*;
import com.group.pet_service.model.PetService;
import com.group.pet_service.repository.RoleRepository;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.utils.Helper;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    Cloudinary cloudinary;
    private final UserImageRepository userImageRepository;


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponse> getUsers(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }


    public UserResponse getUser(String username){
        return userMapper.toUserResponse(userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED)));
    }

    public UserResponse  updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        //userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void addStaffImg(String staffId, MultipartFile[] images) throws IOException {
        // Find the PetService by ID or throw an exception if not found
        User user = userRepository.findById(staffId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
//        testCloudinary();
        Set<UserImage> userImages = new HashSet<>();

        for (MultipartFile image : images) {
            try {
                // Upload image to Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader()
                        .upload(image.getBytes(), ObjectUtils.asMap("folder", "pet_service_images/"));

                // Extract the secure URL of the uploaded image
                String url = (String) uploadResult.get("secure_url");

                // Create a ServiceImage entity and associate it with the PetService
                UserImage userImage = UserImage.builder()
                        .url(url)
                        .user(user)
                        .build();

                userImages.add(userImage);
            } catch (Exception e) {
                throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        userImageRepository.saveAll(userImages);
    }
}
