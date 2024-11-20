package com.group.pet_service.controller;

import com.group.pet_service.dto.request.PetRequest;
import com.group.pet_service.dto.request.ServiceCreationRequest;
import com.group.pet_service.dto.request.SpeciesRequest;
import com.group.pet_service.dto.request.UserCreationRequest;
import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.enums.RoleEnum;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.service.*;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminController {
    private final UserRepository userRepository;

    PetServiceService  petServiceService;
    AuthenticationService authenticationService;
    SpeciesService speciesService;
    PetService petService;
    UserService userService;

    private static final String UPLOAD_DIR = "src/main/resources/static/img/"; // Folder where images will be stored


    @GetMapping("/admin")
    public String showPage(Model model) {
        // Interacting with service to get data
        String message = "Hello, Chris!";
        model.addAttribute("message", message); // Passing data to the view

        return "AdminPage"; // returns the Thymeleaf template name
    }

    @GetMapping("/add-staff")
    public String addStaff(Model model){
        model.addAttribute("user", new UserCreationRequest());
        return "addStaff";
    }

    @PostMapping("/add-staff")
    public String addStaff(@ModelAttribute UserCreationRequest request, Model model) {
        try {
            authenticationService.signup(request, RoleEnum.STAFF);
        } catch (AppException | MessagingException e) {
            // Catch exceptions and pass an error message to the frontend
            model.addAttribute("errorMessage", "Error while adding staff: " + e.getMessage());
            return "addStaff";  // Redirect back to the admin page or wherever you want to show the error
        }
        return "redirect:/admin";
    }


    @GetMapping("/add-service")
    public String showServiceForm(Model model) {
        model.addAttribute("serviceCreationRequest", new ServiceCreationRequest()); // Add the model object to be used in Thymeleaf
        return "addService";  // Thymeleaf template file name without extension
    }

    @PostMapping("/add-service")
    public String addService(@ModelAttribute ServiceCreationRequest request, Model model) {
        try {
            petServiceService.createService(request);
        } catch (AppException e) {
            // Catch the exception and add the error message and form data to the model
            model.addAttribute("errorMessage", "Error while adding service: " + e.getMessage());
            model.addAttribute("serviceCreationRequest", request);  // Add form data to the model
            return "addService";  // Return to the addService page
        }
        return "redirect:/admin";  // Redirect to the admin page after successful service addition
    }


    @GetMapping("/add-service-image")
    public String addServiceImg(Model model){
        return "addServiceImg";
    }

    @PostMapping("/add-service-image")
    public String addServiceImage(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("images") MultipartFile[] images,
            Model model) {

        try {
            // Add images to the service by calling the service layer
            petServiceService.addImagesToService(serviceId, images);
        } catch (AppException | IOException e) {
            // Catch the exception and add error message to the model
            model.addAttribute("errorMessage", "Error while adding images: " + e.getMessage());
            return "addServiceImg";  // Stay on the same page, and show the error message
        }

        // Redirect to the admin page (or another appropriate page) after success
        return "redirect:/admin";
    }


    //------------------------------------------------------------------------------------------
    @GetMapping("/add-species")
    public String addSpecies(Model model){
        model.addAttribute("speciesRequest", new SpeciesRequest());
        return "addSpecies";
    }

    @PostMapping("/add-species")
    public String addSpecies(@ModelAttribute SpeciesRequest request, Model model) {
        try {
            speciesService.createSpecies(request);
        } catch (AppException e) {
            // Catch the exception and add error message to the model
            model.addAttribute("errorMessage", "Error while adding species: " + e.getMessage());
            return "addSpecies";  // Stay on the same page to show the error message
        }
        return "redirect:/admin";  // Redirect to the admin page after success
    }



    @GetMapping("/add-pet")
    public String addPet(Model model){
        model.addAttribute("petRequest", new PetRequest());
        return "addPet";
    }

    @PostMapping("/add-pet")
    public String addPet(@ModelAttribute PetRequest request, Model model) {
        try {
            petService.createPet(request);
        } catch (AppException e) {
            // Catch the exception and add the error message to the model
            model.addAttribute("errorMessage", "Error while adding pet: " + e.getMessage());
            return "addPet";  // Return to the pet form page (or another appropriate page)
        }
        return "redirect:/admin";  // Redirect to the admin page after success
    }


    @GetMapping("/add-pet-image")
    public String addPetImg(Model model){
        return "addPetImg";
    }

    @PostMapping("/add-pet-image")
    public String addPetImg(
            @RequestParam("petId") String petId,
            @RequestParam("images") MultipartFile[] images,
            Model model) {

        try {
            // Add images to the pet by calling the service layer
            petService.addImagesToPet(petId, images);
        } catch (AppException | IOException e) {
            // Catch the exception and add the error message to the model
            model.addAttribute("errorMessage", "Error while adding images: " + e.getMessage());
            return "addPetImg";  // Redirect back to the pet image upload page (or another appropriate page)
        }

        // Redirect to the admin page after success
        return "redirect:/admin";
    }


    @GetMapping("/add-staff-image")
    public String addStaffImage(Model model){
        return "addStaffImg";
    }

    @PostMapping("/add-staff-image")
    public String addStaffImage(@RequestParam("staffId") String staffId,
                                @RequestParam("images") MultipartFile[] images,
                                Model model) {
        try {
            // Add images to the service by calling the service layer
            userService.addStaffImg(staffId, images);
        } catch (IOException | AppException e) {
            // Catch exceptions and pass an error message to the frontend
            model.addAttribute("errorMessage", "Image upload failed: " + e.getMessage());
            return "addStaffImg";  // Return to the admin page or wherever you want to show the error
        }

        // Redirect to the service details page (or another appropriate page)
        return "redirect:/admin";
    }

}