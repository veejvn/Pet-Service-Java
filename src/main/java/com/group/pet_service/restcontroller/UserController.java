package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.staff.StaffResponse;
import com.group.pet_service.dto.user.UserUpdateRequest;
import com.group.pet_service.dto.api.ApiResponse;
import com.group.pet_service.dto.user.UserResponse;
import com.group.pet_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateInfo(@RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("user-s-01")
                .message("Updated user info successfully")
                .data(userService.updateInfo(request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getUser() {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("user-s-02")
                .message("Get user info successfully")
                .data(userService.getUser())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        userService.delete(userId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("user-s-03")
                .message("Delete user successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/staffs")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getStaffs() {
        ApiResponse<List<StaffResponse>> apiResponse = ApiResponse.<List<StaffResponse>>builder()
                .code("user-s-04")
                .message("Get staffs successfully")
                .data(userService.findAllStaffs())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
