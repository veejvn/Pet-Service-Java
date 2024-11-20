package com.group.pet_service.service;

import com.group.pet_service.dto.auth.ChangePasswordRequest;
import com.group.pet_service.dto.auth.ForgotPasswordRequest;
import com.group.pet_service.dto.auth.UserInfoResponse;
import com.group.pet_service.dto.auth.VerifyForgotPasswordRequest;
import com.group.pet_service.dto.jwt.JWTPayloadDto;
import com.group.pet_service.dto.request.*;
import com.group.pet_service.dto.response.AuthResponse;
import com.group.pet_service.dto.response.UserResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.enums.Role;
import com.group.pet_service.mapper.UserMapper;
import com.group.pet_service.model.RefreshToken;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.RefreshTokenRepository;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.util.PasswordUtil;
import com.group.pet_service.util.UserUtil;
import com.group.pet_service.util.jwt.AccessTokenUtil;
import com.group.pet_service.util.jwt.RefreshTokenUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    AccessTokenUtil accessTokenUtil;
    RefreshTokenUtil refreshTokenUtil;
    RefreshTokenRepository refreshTokenRepository;
    UserMapper userMapper;
    UserUtil userUtil;
    PasswordUtil passwordUtil;

    public void register(AuthRequest request){
        boolean existedUser = userRepository.existsByEmail(request.getEmail());
        if(existedUser){
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed", "auth-e-01");
        }
    }

    public AuthResponse verifyRegister(AuthRequest request){
        register(request);

        String hashedPassword = passwordUtil.encodePassword(request.getPassword());
        request.setPassword(hashedPassword);

        Set<Role> roles = new HashSet<>();

        roles.add(Role.USER);

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        user.setRoles(roles);
        userRepository.save(user);

        String accessToken = accessTokenUtil.generateToken(userMapper.toJWTPayloadDto(user));
        String refreshToken = refreshTokenUtil.generateToken(userMapper.toJWTPayloadDto(user), user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse login(AuthLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Email not found", "auth-e-02"));

        if (!user.getRoles().contains(request.getRole())) {
            throw new AppException(HttpStatus.FORBIDDEN, "Insufficient permissions", "auth-e-03");
        }

        boolean isMatchPassword = passwordUtil.checkPassword(request.getPassword(), user.getPassword());

        if (!isMatchPassword) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Wrong password", "auth-e-04");
        }

        String accessToken = accessTokenUtil.generateToken(userMapper.toJWTPayloadDto(user));
        String refreshToken = refreshTokenUtil.generateToken(userMapper.toJWTPayloadDto(user), user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(LogoutRequest request){
        JWTPayloadDto jwtPayloadDto = refreshTokenUtil.verifyToken(request.getRefreshToken());
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(jwtPayloadDto.getId())
                .orElseThrow(
                    () -> new AppException(HttpStatus.NOT_FOUND, "Refresh token not found", "auth-e-05")
                );
        refreshToken.setToken(null);
        refreshTokenRepository.save(refreshToken);
    }

    public AuthResponse refreshToken(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        JWTPayloadDto jwtPayloadDto = refreshTokenUtil.verifyToken(refreshToken);
        String accessToken = accessTokenUtil.generateToken(jwtPayloadDto);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public void changePassword(ChangePasswordRequest request){
        String userId = userUtil.getUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Account not found", "auth-e-06")
        );
        boolean isMatchPassword = passwordUtil.checkPassword(request.getCurrentPassword(), user.getPassword());
        if(!isMatchPassword){
            throw new AppException(HttpStatus.BAD_REQUEST, "Wrong current password", "auth-e-07");
        }
        String newPassword = passwordUtil.encodePassword(request.getNewPassword());
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Email not found", "auth-e-02")
        );
    }

    public AuthResponse verifyForgotPassword(String email, VerifyForgotPasswordRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Email not found", "auth-e-02")
        );
        String hashedPassword = passwordUtil.encodePassword(request.getNewPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        String accessTokenString = accessTokenUtil.generateToken(userMapper.toJWTPayloadDto(user));
        String refreshTokenString = refreshTokenUtil.generateToken(userMapper.toJWTPayloadDto(user), user);
        return AuthResponse.builder()
                .accessToken(accessTokenString)
                .refreshToken(refreshTokenString)
                .build();
    }

    public UserInfoResponse getUserInfo() {
        String userId = userUtil.getUserId();
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "User not found", "auth-e-06")
                );
        return userMapper.toUserInfoResponse(user);
    }
}
