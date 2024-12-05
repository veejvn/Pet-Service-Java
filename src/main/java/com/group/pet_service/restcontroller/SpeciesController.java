package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.species.SpeciesRequest;
import com.group.pet_service.dto.api.ApiResponse;
import com.group.pet_service.dto.species.SpeciesResponse;
import com.group.pet_service.service.SpeciesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/species")
@RequiredArgsConstructor
public class SpeciesController {

    private final SpeciesService speciesService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpeciesResponse>>> getAll() {
        ApiResponse<List<SpeciesResponse>> apiResponse = ApiResponse.<List<SpeciesResponse>>builder()
                .code("species-s-1")
                .message("Get all species successfully")
                .data(speciesService.getAll())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpeciesResponse>> get(@PathVariable String id) {
        ApiResponse<SpeciesResponse> apiResponse = ApiResponse.<SpeciesResponse>builder()
                .code("species-s-2")
                .message("Get species successfully")
                .data(speciesService.get(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
