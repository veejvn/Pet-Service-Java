package com.group.pet_service.restcontroller;

import com.cloudinary.Api;
import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.dto.response.ServiceResponse;
import com.group.pet_service.model.PetService;
import com.group.pet_service.service.PetServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
	private final PetServiceService serviceService;
	@GetMapping
	public ApiResponse<List<ServiceResponse>> getAllServices() {
		return ApiResponse.<List<ServiceResponse>>builder()
				.result(serviceService.getAll())
				.build();
	}
}