package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.request.*;
import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.dto.response.AuthenticationResponse;
import com.group.pet_service.dto.response.UserResponse;
import com.group.pet_service.enums.RoleEnum;
import com.group.pet_service.service.AuthenticationService;
import com.group.pet_service.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/signin")
    ApiResponse<AuthenticationResponse> signin(@RequestBody AuthenticationRequest request){
        var result = authenticationService.login(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/signup")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) throws MessagingException {
        return ApiResponse.<UserResponse>builder()
                .result(authenticationService.signup(request, RoleEnum.USER))
                .build();
    }

    @PostMapping("/verify")
    public ApiResponse verify(
            @RequestBody UserVerifyCodeRequest request
    )  {
        authenticationService.verifyCode(request);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Verify Email Success")
                .build();
    }

    @PostMapping("/resend")
    public ApiResponse resendVerifyCode(
            @RequestBody ResendCodeRequest request
    ) throws MessagingException {
        authenticationService.resendVerifyCode(request);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Resend verify code Success")
                .build();
    }


    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refreshToken")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
}

