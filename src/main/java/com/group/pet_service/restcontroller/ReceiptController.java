package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.pet_service_item.ChangeStatusPetServiceItemRequest;
import com.group.pet_service.dto.pet_service_item.PetServiceItemResponse;
import com.group.pet_service.dto.receipt.ReceiptCreateRequest;
import com.group.pet_service.dto.api.ApiResponse;
import com.group.pet_service.dto.receipt.ReceiptResponse;
import com.group.pet_service.service.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReceiptResponse>> create(@RequestBody @Valid ReceiptCreateRequest request) {
        ApiResponse<ReceiptResponse> apiResponse = ApiResponse.<ReceiptResponse>builder()
                .code("receipt-s-1")
                .message("Create receipt successfully")
                .data(receiptService.create(request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReceiptResponse>>> getAll() {
        ApiResponse<List<ReceiptResponse>> apiResponse = ApiResponse.<List<ReceiptResponse>>builder()
                .code("receipt-s-2")
                .message("Create receipt successfully")
                .data(receiptService.getAll())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<ReceiptResponse>>> getAllByStaff() {
        ApiResponse<List<ReceiptResponse>> apiResponse = ApiResponse.<List<ReceiptResponse>>builder()
                .code("receipt-s-3")
                .message("Get receipt by staff successfully")
                .data(receiptService.getReceiptByStaff())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/change-status/staff/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PetServiceItemResponse>> staffChangeStatusPetServiceItem(@PathVariable("id") String id, @RequestBody @Valid ChangeStatusPetServiceItemRequest request) {
        ApiResponse<PetServiceItemResponse> apiResponse = ApiResponse.<PetServiceItemResponse>builder()
                .code("receipt-s-4")
                .message("Change status receipt successfully")
                .data(receiptService.staffChangeStatusPetServiceItem(id, request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/change-status/user/{id}")
    public ResponseEntity<ApiResponse<PetServiceItemResponse>> userChangeStatusPetServiceItem(@PathVariable("id") String id) {
        ApiResponse<PetServiceItemResponse> apiResponse = ApiResponse.<PetServiceItemResponse>builder()
                .code("receipt-s-4")
                .message("Change status receipt successfully")
                .data(receiptService.userChangeStatusPetServiceItem(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
