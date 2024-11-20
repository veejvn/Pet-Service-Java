package com.group.pet_service.restcontroller;

import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.dto.response.PetResponse;
import com.group.pet_service.dto.response.SpeciesResponse;
import com.group.pet_service.service.PetService;
import com.group.pet_service.service.SpeciesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/species")
@RequiredArgsConstructor
public class SpeciesController {

    private final SpeciesService speciesService;
    @GetMapping
    public ApiResponse<List<SpeciesResponse>> getAll(){
        return ApiResponse.<List<SpeciesResponse>>builder()
                .result(speciesService.getAll())
                .build();
    }
}
