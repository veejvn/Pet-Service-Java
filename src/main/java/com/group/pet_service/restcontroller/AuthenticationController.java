package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.auth.*;
import com.group.pet_service.dto.api.ApiResponse;
import com.group.pet_service.dto.auth.AuthResponse;
import com.group.pet_service.dto.token.RefreshRequest;
import com.group.pet_service.service.AuthenticationService;
import com.group.pet_service.service.EmailService;
import com.group.pet_service.util.CodeUtil;
import com.group.pet_service.util.CommonUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    @Value("${app.clientReceiveTokensPath}")
    @NonFinal
    String clientReceiveTokensPath;
    AuthenticationService authenticationService;
    CodeUtil<AuthRequest> codeUtil;
    CodeUtil<String> forgotPasswordCodeUtil;
    @NonFinal
    CommonUtil commonUtil;
    EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid AuthRequest request) {
        authenticationService.register(request);
        String verificationCode = UUID.randomUUID().toString();
        codeUtil.save(verificationCode, request, 10);
        emailService.sendEmailToVerifyRegister(request.getEmail(), verificationCode);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-01")
                .message("Request register successfully, check your email")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/register/verify/{verificationCode}")
    public RedirectView verifyRegister(@PathVariable String verificationCode) {
        AuthRequest request = codeUtil.get(verificationCode);
        AuthResponse authResponse = authenticationService.verifyRegister(request);
        codeUtil.remove(verificationCode);
        String redirectUrl = UriComponentsBuilder.fromUriString(clientReceiveTokensPath)
                .queryParam("accessToken", authResponse.getAccessToken())
                .queryParam("refreshToken", authResponse.getRefreshToken())
                .build()
                .toUriString();
        return new RedirectView(redirectUrl);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid AuthLoginRequest request) {
        var result = authenticationService.login(request);
        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .code("auth-s-02")
                .message("Authenticate successfully")
                .data(result)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Valid LogoutRequest request) {
        authenticationService.logout(request);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-03")
                .message("Log out successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody @Valid RefreshRequest request) {
        AuthResponse authResponse = authenticationService.refreshToken(request);
        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .code("auth-s-4")
                .message("Refresh new access token successfully")
                .data(authResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-05")
                .message("Password changed successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forGotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authenticationService.forgotPassword(request);
        String verificationCode = CommonUtil.generateVerificationCode();
        forgotPasswordCodeUtil.save(CommonUtil.getForgotPasswordKey(verificationCode), request.getEmail(), 3);
        emailService.sendEmailToVerifyForgotPassword(request.getEmail(), verificationCode);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-06")
                .message("Request to get new password successfully, please check your email")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/forgot-password/verify")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyForgotPassword(@RequestBody @Valid VerifyForgotPasswordRequest request) {
        String email = forgotPasswordCodeUtil.get(CommonUtil.getForgotPasswordKey(request.getCode()));
        AuthResponse authResponse = authenticationService.verifyForgotPassword(email, request);
        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .code("auth-s-07")
                .message("Verify forgot password successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getInfo() {
        ApiResponse<UserInfoResponse> apiResponse = ApiResponse.<UserInfoResponse>builder()
                .data(authenticationService.getUserInfo())
                .code("auth-s-8")
                .message("Get user info successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

