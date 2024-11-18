package com.group.pet_service.controller;

import java.util.HashMap;
import java.util.Map;

import com.group.pet_service.dto.response.ApiResponse;
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

import com.group.pet_service.dto.request.ServiceRequest;
import com.group.pet_service.service.ServiceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
	private final ServiceService serviceService;

	/* Lấy trang đầu tiên 20 phần tử/api/v1/services?page=0&size=20 */
	@GetMapping
	public ResponseEntity<ApiResponse<Map<String, Object>>> getAllServices(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<ServiceRequest> servicePage = serviceService.getAllServices(pageable);

		Map<String, Object> response = new HashMap<>();
		response.put("services", servicePage.getContent());
		response.put("currentPage", servicePage.getNumber());
		response.put("totalItems", servicePage.getTotalElements());
		response.put("totalPages", servicePage.getTotalPages());

		ApiResponse<Map<String, Object>> apiResponse = ApiResponse.<Map<String, Object>>builder()
				.code("service-s-01")
				.message("Get all service successfully")
				.result(response)
				.build();

		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ServiceRequest>> getServiceById(@PathVariable String id) {
		ServiceRequest service = serviceService.getServiceById(id);
		ApiResponse<ServiceRequest> apiResponse = ApiResponse.<ServiceRequest>builder()
				.code("service-s-02")
				.message("Get service successfully")
				.result(service)
				.build();

		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<ServiceRequest>> createService(@Valid @RequestBody ServiceRequest serviceRequest) {
		ServiceRequest createdService = serviceService.createService(serviceRequest);
		ApiResponse<ServiceRequest> apiResponse = ApiResponse.<ServiceRequest>builder()
				.code("service-s-03")
				.message("Create service successfully")
				.result(createdService)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ServiceRequest>> updateService(@PathVariable String id,
																	 @Valid @RequestBody ServiceRequest serviceRequest) {
		// Gọi phương thức updateService để cập nhật dịch vụ

		ServiceRequest updatedService = serviceService.updateService(id, serviceRequest);
		ApiResponse<ServiceRequest> apiResponse = ApiResponse.<ServiceRequest>builder()
				.code("service-s-04")
				.message("Update service successfully")
				.result(updatedService)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable String id) {
		serviceService.deleteService(id);
		ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
				.code("service-s-05")
				.message("Delete service successfully").build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
}