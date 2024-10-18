package com.group.pet_service.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.group.pet_service.dto.serviceDto.ServiceDTO;
import com.group.pet_service.service.ServiceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {
	private final ServiceService serviceService;

	/* Lấy trang đầu tiên 20 phần tử/api/v1/services?page=0&size=20 */
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllServices(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<ServiceDTO> servicePage = serviceService.getAllServices(pageable);

		Map<String, Object> response = new HashMap<>();
		response.put("services", servicePage.getContent());
		response.put("currentPage", servicePage.getNumber());
		response.put("totalItems", servicePage.getTotalElements());
		response.put("totalPages", servicePage.getTotalPages());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ServiceDTO> getServiceById(@PathVariable String id) {
		ServiceDTO service = serviceService.getServiceById(id);
		return ResponseEntity.ok(service);
	}

	@PostMapping
	public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceDTO serviceDTO) {
		ServiceDTO createdService = serviceService.createService(serviceDTO);
		return new ResponseEntity<>(createdService, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ServiceDTO> updateService(@PathVariable String id,
			@Valid @RequestBody ServiceDTO serviceDTO) {
		// Gọi phương thức updateService để cập nhật dịch vụ
		ServiceDTO updatedService = serviceService.updateService(id, serviceDTO);
		return ResponseEntity.ok(updatedService);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteService(@PathVariable String id) {
		serviceService.deleteService(id);
		return ResponseEntity.noContent().build();
	}
}