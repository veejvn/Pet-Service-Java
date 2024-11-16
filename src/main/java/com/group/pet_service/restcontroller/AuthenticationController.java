package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.request.*;
import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.dto.response.AuthResponse;
import com.group.pet_service.dto.response.IntrospectResponse;
import com.group.pet_service.service.AuthenticationService;
import com.group.pet_service.service.EmailService;
import com.group.pet_service.util.CodeUtil;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
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
    EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid AuthRequest request){
        authenticationService.register(request);
        String verificationCode = UUID.randomUUID().toString();
        codeUtil.save(verificationCode, request, 1);
        emailService.sendEmailToVerifyRegister(request.getEmail(),verificationCode);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-01")
                .message("Request register successfully, check your email")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/register/verify/{verificationCode}")
    public RedirectView verifyRegister(@PathVariable String verificationCode){
        AuthRequest request = codeUtil.get(verificationCode);
        AuthResponse authResponse = authenticationService.verifyRegister(request);
        codeUtil.remove(verificationCode);
        String redirectUrl = UriComponentsBuilder.fromUriString(clientReceiveTokensPath)
                .queryParam("token", authResponse.getToken())
                .toString();
        return new RedirectView(redirectUrl);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid AuthLoginRequest request){
        var result = authenticationService.login(request);
        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .code("auth-s-02")
                .message("Authenticate successfully")
                .result(result)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Valid LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-03")
                .message("Log out successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
         AuthResponse authResponse = authenticationService.refreshToken(request);
         ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .code("auth-s-4")
                .message("Refresh new access token successfully")
                .result(authResponse)
                .build();
         return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

