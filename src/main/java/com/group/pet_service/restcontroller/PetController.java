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
    @GetMapping
    public ApiResponse<List<PetResponse>> getAll(){
        return ApiResponse.<List<PetResponse>>builder()
                .result(petService.getAll())
                .build();
    }
}
