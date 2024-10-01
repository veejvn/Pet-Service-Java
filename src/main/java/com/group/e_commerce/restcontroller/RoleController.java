package com.group.e_commerce.restcontroller;

import com.group.e_commerce.dto.request.RoleRequest;
import com.group.e_commerce.dto.request.UserCreationRequest;
import com.group.e_commerce.dto.request.UserUpdateRequest;
import com.group.e_commerce.dto.response.ApiResponse;
import com.group.e_commerce.dto.response.RoleResponse;
import com.group.e_commerce.dto.response.UserResponse;
import com.group.e_commerce.mapper.UserMapper;
import com.group.e_commerce.service.RoleService;
import com.group.e_commerce.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    public ApiResponse<Void> delete(@PathVariable String role){
        roleService.delete(role);
        return  ApiResponse.<Void>builder().build();
    }
}
