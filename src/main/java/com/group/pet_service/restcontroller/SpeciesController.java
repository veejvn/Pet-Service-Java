package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.request.PetRequest;
import com.group.pet_service.dto.request.SpeciesRequest;
import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.dto.response.PetResponse;
import com.group.pet_service.dto.response.SpeciesResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<SpeciesResponse>> create(@RequestBody @Valid SpeciesRequest request){
        ApiResponse<SpeciesResponse> apiResponse = ApiResponse.<SpeciesResponse>builder()
                .code("species-s-1")
                .message("Create species successfully")
                .data(speciesService.create(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpeciesResponse>>> getAll(){
        ApiResponse<List<SpeciesResponse>> apiResponse = ApiResponse.<List<SpeciesResponse>>builder()
                .code("species-s-2")
                .message("Get all species successfully")
                .data(speciesService.getAll())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpeciesResponse>> get(@PathVariable String id){
        ApiResponse<SpeciesResponse> apiResponse = ApiResponse.<SpeciesResponse>builder()
                .code("species-s-3")
                .message("Get species successfully")
                .data(speciesService.get(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id){
        speciesService.delete(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("species-s-4")
                .message("Delete species successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
