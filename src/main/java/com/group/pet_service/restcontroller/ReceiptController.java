package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.request.ReceiptCreateRequest;
import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.dto.response.ReceiptResponse;
import com.group.pet_service.service.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReceiptResponse>> create(@RequestBody @Valid ReceiptCreateRequest request){
        ApiResponse<ReceiptResponse> apiResponse = ApiResponse.<ReceiptResponse>builder()
                .code("receipt-s-1")
                .message("Create receipt successfully")
                .data(receiptService.create(request))
                .build();
                return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
