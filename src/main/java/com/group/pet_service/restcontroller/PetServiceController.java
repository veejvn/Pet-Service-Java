package com.group.pet_service.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.group.pet_service.dto.api.ApiResponse;
import com.group.pet_service.dto.pet_service.PetServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group.pet_service.dto.pet.PetServiceRequest;
import com.group.pet_service.service.PetServiceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pet-services")
@RequiredArgsConstructor
public class PetServiceController {
    private final PetServiceService petServiceService;

    /* Lấy trang đầu tiên 20 phần tử/api/services?page=0&size=20 */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllPetServices(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<PetServiceResponse> servicePage = petServiceService.getAllServices(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("services", servicePage.getContent());
        response.put("currentPage", servicePage.getNumber());
        response.put("totalItems", servicePage.getTotalElements());
        response.put("totalPages", servicePage.getTotalPages());

        ApiResponse<Map<String, Object>> apiResponse = ApiResponse.<Map<String, Object>>builder()
                .code("service-s-01")
                .message("Get all service successfully")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PetServiceResponse>> getPetServiceById(@PathVariable String id) {
        PetServiceResponse service = petServiceService.getServiceById(id);
        ApiResponse<PetServiceResponse> apiResponse = ApiResponse.<PetServiceResponse>builder()
                .code("service-s-02")
                .message("Get service successfully")
                .data(service)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}