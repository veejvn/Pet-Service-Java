package com.group.pet_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group.pet_service.dto.request.ServiceRequest;
import com.group.pet_service.service.ServiceService;

@RestController
@RequestMapping("/")
public class HomeController {

    private final ServiceService serviceService;

    @Autowired
    public HomeController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping()
    public ResponseEntity<List<ServiceRequest>> getLatestServices() {
        List<ServiceRequest> latestServices = serviceService.getLatestServices(5);
        return ResponseEntity.ok(latestServices);
    }
}
