package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.request.PetRequest;
import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.dto.response.PetResponse;
import com.group.pet_service.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<ApiResponse<PetResponse>> create(@RequestBody @Valid PetRequest request){
        ApiResponse<PetResponse> apiResponse = ApiResponse.<PetResponse>builder()
                .code("pet-s-1")
                .message("Create pet successfully")
                .data(petService.create(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PetResponse>> get(@PathVariable String id){
        ApiResponse<PetResponse> apiResponse = ApiResponse.<PetResponse>builder()
                .code("pet-s-2")
                .message("Get pet successfully")
                .data(petService.get(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PetResponse>>> getAll(){
        ApiResponse<List<PetResponse>> apiResponse = ApiResponse.<List<PetResponse>>builder()
                .code("pet-s-3")
                .message("Get pets successfully")
                .data(petService.getAll())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PetResponse>> update(@RequestBody @Valid PetRequest request, @PathVariable String id){
        ApiResponse<PetResponse> apiResponse = ApiResponse.<PetResponse>builder()
                .code("pet-s-4")
                .message("Update pet successfully")
                .data(petService.update(request, id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id){
        petService.delete(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("pet-s-5")
                .message("Delete pet successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
