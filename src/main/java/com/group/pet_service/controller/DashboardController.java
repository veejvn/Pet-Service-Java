package com.group.pet_service.controller;

import com.group.pet_service.dto.admin.JobPositionRequest;
import com.group.pet_service.dto.admin.JobPositionResponse;
import com.group.pet_service.dto.admin.StaffEditRequest;
import com.group.pet_service.dto.admin.StaffResponse;
import com.group.pet_service.dto.pet.PetServiceRequest;
import com.group.pet_service.dto.pet_service.PetServiceResponse;
import com.group.pet_service.dto.receipt.ReceiptResponse;
import com.group.pet_service.dto.species.SpeciesRequest;
import com.group.pet_service.dto.species.SpeciesResponse;
import com.group.pet_service.dto.admin.StaffCreationRequest;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.service.*;
import com.group.pet_service.service.impl.UserDetailsImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class DashboardController {
    private final UserRepository userRepository;

    private final PetServiceService petServiceService;
    private final SpeciesService speciesService;
    private final UserService userService;
    private final JobPositionService jobPositionService;
    private final ReceiptService receiptService;


    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String showDashboard(HttpSession session, @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String userId = userDetails.getId();
            User user = userRepository.findById(userId).orElse(null);
            session.setAttribute("user", user);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<ReceiptResponse> receiptResponsePage = receiptService.findAll(pageable);
        model.addAttribute("listReceipt", receiptResponsePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", receiptResponsePage.getTotalPages());
        return "Dashboard";
    }

    @GetMapping("/add-staff")
    @PreAuthorize("hasRole('ADMIN')")
    public String addStaff(Model model) {
        List<JobPositionResponse> jobPositions = jobPositionService.findAll();
        model.addAttribute("staffCreateRequest", new StaffCreationRequest());
        model.addAttribute("jobPositions", jobPositions);
        return "AddStaff";
    }

    @PostMapping("/add-staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addStaff(@ModelAttribute("staffCreateRequest") @Valid StaffCreationRequest request,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            modelAndView.setViewName("redirect:/admin/add-staff");
            return modelAndView;
        }
        try {
            userService.addStaff(request);
            redirectAttributes.addFlashAttribute("successMessage", "Add staff successfully");
            modelAndView.setViewName("redirect:/admin/list-staff");
        } catch (AppException | IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/add-staff");
        }
        return modelAndView;
    }

    @GetMapping("/list-staff")
    @PreAuthorize("hasRole('ADMIN')")
    public String showListStaff(Model model) {
        List<StaffResponse> staffResponses = userService.findAll();
        model.addAttribute("listStaff", staffResponses);
        return "ListStaff";
    }

    @GetMapping("/edit-staff/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showPageEditStaff(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            StaffEditRequest request = userService.findById(id);
            List<JobPositionResponse> jobPositions = jobPositionService.findAll();
            model.addAttribute("staffEditRequest", request);
            model.addAttribute("jobPositions", jobPositions);
            return "EditStaff";
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/list-staff";
        }
    }

    @PostMapping("/edit-staff/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editStaff(@ModelAttribute("staffEditRequest") StaffEditRequest request, @PathVariable("id") String id,
                                  RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            userService.updateStaff(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Update staff successfully");
            modelAndView.setViewName("redirect:/admin/list-staff");
        } catch (AppException | IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/edit-staff/" + id);
        }
        return modelAndView;
    }

    @PostMapping("/delete-staff/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteStaff(@PathVariable String id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Delete staff successfully");
            modelAndView.setViewName("redirect:/admin/list-staff");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/list-staff");
        }
        return modelAndView;
    }

    @GetMapping("/add-pet-service")
    @PreAuthorize("hasRole('ADMIN')")
    public String showPagePetService(Model model) {
        model.addAttribute("petServiceRequest", new PetServiceRequest());
        return "AddPetService";
    }

    @PostMapping("/add-pet-service")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addPetService(@ModelAttribute PetServiceRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            petServiceService.createPetService(request);
            redirectAttributes.addFlashAttribute("successMessage", "Add Pet Service Successfully");
            modelAndView.setViewName("redirect:/admin/list-pet-service");
        } catch (AppException | IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/add-pet-service");
        }
        return modelAndView;
    }

    @GetMapping("/list-pet-service")
    @PreAuthorize("hasRole('ADMIN')")
    public String showListPetService(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PetServiceResponse> petServiceResponsePage = petServiceService.findAll(pageable);
        model.addAttribute("listPetService", petServiceResponsePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", petServiceResponsePage.getTotalPages());
        return "ListPetService";
    }

    //    @GetMapping("/edit-service/{id}")
//    public String editService(@PathVariable String id, Model model) {
//        try {
//            // Fetch the service data by ID
//            PetService existingService = petServiceService.getServiceById(id);
//
//            // Map the PetService entity to the ServiceEditRequest DTO (if needed)
//            ServiceEditRequest serviceEditRequest = ServiceEditRequest.builder()
//                    .id(existingService.getId())
//                    .name(existingService.getName())
//                    .description(existingService.getDescription())
//                    .price(existingService.getPrice())
//                    .disabled(existingService.isDisabled())
//                    .build();
//
//            // Add the service data to the model
//            model.addAttribute("service", serviceEditRequest);
//        } catch (AppException e) {
//            // Handle case when the service is not found
//            model.addAttribute("errorMessage", "Service not found: " + e.getMessage());
//        }
//
//        return "editService";  // Show the edit form with service data pre-filled
//    }
//
//    @PostMapping("/edit-service")
//    public String editService(@ModelAttribute ServiceEditRequest request, Model model) {
//        try {
//            // Attempt to update the service
//            petServiceService.updateService(request);
//        } catch (AppException e) {
//            // If an error occurs, display the error message
//            model.addAttribute("errorMessage", "Error while editing service: " + e.getMessage());
//
//            // Fetch and return the current service data to repopulate the form
//            com.group.pet_service.model.PetService existingService = petServiceService.getServiceById(request.getId());
//            model.addAttribute("service", ServiceEditRequest.builder()
//                    .id(existingService.getId())
//                    .name(existingService.getName())
//                    .description(existingService.getDescription())
//                    .price(existingService.getPrice())
//                    .disabled(existingService.isDisabled())
//                    .build());
//
//            return "editService";  // Stay on the same page with the error message
//        }
//
//        return "redirect:/admin";  // Redirect to the admin page after success
//    }

    @GetMapping("/add-species")
    public String addSpecies(Model model) {
        model.addAttribute("speciesRequest", new SpeciesRequest());
        return "AddSpecies";
    }

    @PostMapping("/add-species")
    public ModelAndView addSpecies(@ModelAttribute SpeciesRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            speciesService.create(request);
            redirectAttributes.addFlashAttribute("successMessage", "Add species successfully");
            modelAndView.setViewName("redirect:/admin/list-species");
        } catch (AppException e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/add-species");
        }
        return modelAndView;
    }

    @GetMapping("/list-species")
    public String showPageListSpecies(Model model) {
        List<SpeciesResponse> species = speciesService.getAll();
        model.addAttribute("listSpecies", species);
        return "ListSpecies";
    }

//    @GetMapping("/edit-species/{id}")
//    public String editSpecies(@PathVariable String id, Model model) {
//        try {
//            // Fetch the species data by ID
//            Species existingSpecies = speciesService.getSpeciesById(id);
//
//            // Map the Species entity to the SpeciesEditRequest DTO (if needed)
//            SpeciesEditRequest speciesEditRequest = SpeciesEditRequest.builder()
//                    .id(existingSpecies.getId())
//                    .name(existingSpecies.getName())
//                    .description(existingSpecies.getDescription())
//                    .build();
//
//            model.addAttribute("request", speciesEditRequest);
//        } catch (AppException e) {
//            model.addAttribute("errorMessage", "Species not found: " + e.getMessage());
//        }
//
//        return "editSpecies";  // Show the edit form with species data pre-filled
//    }
//
//    @PostMapping("/edit-species")
//    public String editSpecies(@ModelAttribute SpeciesEditRequest request, Model model) {
//        try {
//            speciesService.updateSpecies(request);
//        } catch (AppException e) {
//            // Catch exceptions and pass an error message to the frontend
//            model.addAttribute("errorMessage", "Error while editing species: " + e.getMessage());
//            return "editSpecies";  // Redirect back to the edit form page
//        }
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/delete-species/{id}")
//    public String deleteSpecies(@PathVariable String id, Model model) {
//        try {
//            speciesService.deleteSpecies(id);
//        } catch (AppException e) {
//            // Pass an error message to the frontend
//            model.addAttribute("errorMessage", "Error while deleting species: " + e.getMessage());
//            return "admin";  // Return to admin page with an error message
//        }
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/delete-service/{id}")
//    public String deleteService(@PathVariable String id, Model model) {
//        try {
//            petServiceService.deleteService(id);
//        } catch (AppException e) {
//            // Pass an error message to the frontend
//            model.addAttribute("errorMessage", "Error while deleting species: " + e.getMessage());
//            return "admin";  // Return to admin page with an error message
//        }
//        return "redirect:/admin";
//    }

    @GetMapping("/add-job-position")
    @PreAuthorize("hasRole('ADMIN')")
    public String showPageAddJobPosition(Model model) {
        model.addAttribute("jobPositionRequest", new JobPositionRequest());
        return "AddJobPosition";
    }

    @PostMapping("/add-job-position")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addJobPosition(@ModelAttribute("jobPositionRequest") @Valid JobPositionRequest request,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            modelAndView.setViewName("redirect:/admin/add-job-position");
            return modelAndView;
        }
        try {
            jobPositionService.saveJobPosition(request);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm vị trí công việc thành công");
            modelAndView.setViewName("redirect:/admin/add-staff");
            return modelAndView;
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/add-job-position");
        }
        return modelAndView;
    }

//    @GetMapping("/list-job-position")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String showListJobPosition(Model model) {
//        List<JobPositionResponse> responses = jobPositionService.findAll();
//        model.addAttribute("listJobPosition", responses);
//        return "ListJobPosition";
//    }

}