package com.group.pet_service.restcontroller;

import java.util.List;

import com.group.pet_service.dto.pet_service.PetServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group.pet_service.service.PetServiceService;

@RestController
@RequestMapping("/")
public class HomeController {

    private final PetServiceService petServiceService;

    @Autowired
    public HomeController(PetServiceService petServiceService) {
        this.petServiceService = petServiceService;
    }

    @GetMapping()
    public ResponseEntity<List<PetServiceResponse>> getLatestServices() {
        List<PetServiceResponse> latestServices = petServiceService.getLatestServices(5);
        return ResponseEntity.ok(latestServices);
    }
}
