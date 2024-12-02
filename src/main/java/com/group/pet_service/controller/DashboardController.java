package com.group.pet_service.controller;

import com.group.pet_service.dto.job_position.JobPositionEditRequest;
import com.group.pet_service.dto.job_position.JobPositionRequest;
import com.group.pet_service.dto.job_position.JobPositionResponse;
import com.group.pet_service.dto.pet.PetServiceRequest;
import com.group.pet_service.dto.pet_service.PetServiceEditRequest;
import com.group.pet_service.dto.pet_service.PetServiceResponse;
import com.group.pet_service.dto.receipt.ReceiptResponse;
import com.group.pet_service.dto.species.SpeciesEditRequest;
import com.group.pet_service.dto.species.SpeciesRequest;
import com.group.pet_service.dto.species.SpeciesResponse;
import com.group.pet_service.dto.staff.StaffCreationRequest;
import com.group.pet_service.dto.staff.StaffEditRequest;
import com.group.pet_service.dto.staff.StaffResponse;
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

    @GetMapping("/detail-receipt/{id}")
    public String showPageDetail(@PathVariable("id") String id, Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            ReceiptResponse receiptResponse = receiptService.findById(id);
            model.addAttribute("receiptResponse", receiptResponse);
            return "DetailReceipt";
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/dashboard";
        }

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
        List<StaffResponse> staffResponses = userService.findAllStaffs();
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
            model.addAttribute("id", id);
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

    @GetMapping("/edit-pet-service/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showPageEditPetService(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PetServiceEditRequest petServiceEditRequest = petServiceService.findById(id);
            model.addAttribute("petServiceEditRequest", petServiceEditRequest);
            model.addAttribute("id", id);
            return "EditPetService";
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/list-pet-service";
        }
    }

    @PostMapping("/edit-pet-service/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editPetService(@ModelAttribute("petServiceEditRequest") PetServiceEditRequest request,
                                       @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            petServiceService.updateService(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Update pet service successfully");
            modelAndView.setViewName("redirect:/admin/list-pet-service");
        } catch (AppException | IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/edit-pet-service/" + id);
        }
        return modelAndView;
    }

    @PostMapping("/delete-pet-service/{id}")
    public ModelAndView deletePetService(@PathVariable String id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        petServiceService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Delete Pet service successfully");
        modelAndView.setViewName("redirect:/admin/list-pet-service");
        return modelAndView;
    }

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
    public String showListSpecies(Model model) {
        List<SpeciesResponse> species = speciesService.getAll();
        model.addAttribute("listSpecies", species);
        return "ListSpecies";
    }

    @GetMapping("/edit-species/{id}")
    public String showPageEditSpecies(@PathVariable String id, RedirectAttributes redirectAttributes, Model model) {
        try {
            SpeciesEditRequest speciesEditRequest = speciesService.findById(id);
            model.addAttribute("speciesEditRequest", speciesEditRequest);
            model.addAttribute("id", id);
            return "EditSpecies";
        } catch (AppException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/list-species";
        }
    }

    @PostMapping("/edit-species/{id}")
    public ModelAndView editSpecies(@ModelAttribute("speciesEditRequest") SpeciesEditRequest request, @PathVariable String id,
                                    RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            speciesService.update(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Update species successfully");
            modelAndView.setViewName("redirect:/admin/list-species");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/list-species");
        }
        return modelAndView;
    }

    @PostMapping("/delete-species/{id}")
    public ModelAndView deleteSpecies(@PathVariable String id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            speciesService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Delete species successfully");
            modelAndView.setViewName("redirect:/admin/list-species");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/list-species");
        }
        return modelAndView;
    }

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
            jobPositionService.addJobPosition(request);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm vị trí công việc thành công");
            modelAndView.setViewName("redirect:/admin/list-job-position");
            return modelAndView;
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/add-job-position");
        }
        return modelAndView;
    }

    @GetMapping("/list-job-position")
    @PreAuthorize("hasRole('ADMIN')")
    public String showListJobPosition(Model model) {
        List<JobPositionResponse> responses = jobPositionService.findAll();
        model.addAttribute("listJobPosition", responses);
        return "ListJobPosition";
    }

    @GetMapping("/edit-job-position/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showPageEditJobPosition(@PathVariable String id, Model model) {
        JobPositionEditRequest jobPositionEditRequest = jobPositionService.findById(id);
        model.addAttribute("jobPositionEditRequest", jobPositionEditRequest);
        model.addAttribute("id", id);
        return "EditJobPosition";
    }

    @PostMapping("/edit-job-position/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editJobPosition(@ModelAttribute("jobPositionEditRequest") JobPositionEditRequest request,
                                        @PathVariable String id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            jobPositionService.update(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Edit job position successfully");
            modelAndView.setViewName("redirect:/admin/list-job-position");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            modelAndView.setViewName("redirect:/admin/edit-job-position/" + id);
        }
        ;
        return modelAndView;
    }

    @PostMapping("/delete-job-position/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteJobPosition(@PathVariable String id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        jobPositionService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Delete job position successfully");
        modelAndView.setViewName("redirect:/admin/list-job-position");
        return modelAndView;
    }

}