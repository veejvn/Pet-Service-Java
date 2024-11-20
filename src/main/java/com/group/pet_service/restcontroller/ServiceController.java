package com.group.pet_service.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.dto.response.ServiceResponse;
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

	@PostMapping
	public ResponseEntity<ApiResponse<ServiceResponse>> createService(@Valid @RequestBody ServiceRequest serviceRequest) {
		ServiceResponse createdService = serviceService.createService(serviceRequest);
		ApiResponse<ServiceResponse> apiResponse = ApiResponse.<ServiceResponse>builder()
				.code("service-s-03")
				.message("Create service successfully")
				.data(createdService)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	/* Lấy trang đầu tiên 20 phần tử/api/v1/services?page=0&size=20 */
	@GetMapping
	public ResponseEntity<ApiResponse<Map<String, Object>>> getAllServices(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<ServiceResponse> servicePage = serviceService.getAllServices(pageable);

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
	public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(@PathVariable String id) {
		ServiceResponse service = serviceService.getServiceById(id);
		ApiResponse<ServiceResponse> apiResponse = ApiResponse.<ServiceResponse>builder()
				.code("service-s-02")
				.message("Get service successfully")
				.data(service)
				.build();

		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ServiceResponse>> updateService(@PathVariable String id,
																	 @Valid @RequestBody ServiceRequest serviceRequest) {
		// Gọi phương thức updateService để cập nhật dịch vụ

		ServiceResponse updatedService = serviceService.updateService(id, serviceRequest);
		ApiResponse<ServiceResponse> apiResponse = ApiResponse.<ServiceResponse>builder()
				.code("service-s-04")
				.message("Update service successfully")
				.data(updatedService)
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