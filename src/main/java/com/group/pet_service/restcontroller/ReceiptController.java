package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.receipt.ReceiptCreateRequest;
import com.group.pet_service.dto.api.ApiResponse;
import com.group.pet_service.dto.receipt.ReceiptResponse;
import com.group.pet_service.service.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
