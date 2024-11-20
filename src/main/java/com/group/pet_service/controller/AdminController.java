package com.group.pet_service.controller;

import com.group.pet_service.dto.request.*;
import com.group.pet_service.dto.response.ApiResponse;
import com.group.pet_service.dto.response.AuthenticationResponse;
import com.group.pet_service.enums.RoleEnum;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.UserMapper;
import com.group.pet_service.model.Pet;
import com.group.pet_service.model.Species;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    UserMapper userMapper;

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

    @GetMapping("/edit-staff/{id}")
    public String editStaff(@PathVariable String id, Model model) {
        try {
            // Fetch the user data by ID
            User existingUser = userService.getUserById(id);

            // Map the User entity to the UserEditRequest DTO (if needed)
            UserEditRequest userEditRequest = userMapper.toUserEditRequest(existingUser);

            model.addAttribute("user", userEditRequest);
        } catch (AppException e) {
            model.addAttribute("errorMessage", "User not found: " + e.getMessage());
        }

        return "editStaff";  // Show the edit form with user data pre-filled
    }

    @PostMapping("/edit-staff")
    public String editStaff(@ModelAttribute UserEditRequest request, Model model) {
        try {
            userService.updateStaff(request);
        } catch (AppException e) {
            // Catch exceptions and pass an error message to the frontend
            model.addAttribute("errorMessage", "Error while editing staff: " + e.getMessage());
            return "editStaff";  // Redirect back to the edit form page
        }
        return "redirect:/admin";
    }


    @GetMapping("/edit-service/{id}")
    public String editService(@PathVariable String id, Model model) {
        try {
            // Fetch the service data by ID
            com.group.pet_service.model.PetService existingService = petServiceService.getServiceById(id);

            // Map the PetService entity to the ServiceEditRequest DTO (if needed)
            ServiceEditRequest serviceEditRequest = ServiceEditRequest.builder()
                    .id(existingService.getId())
                    .name(existingService.getName())
                    .description(existingService.getDescription())
                    .price(existingService.getPrice())
                    .disabled(existingService.isDisabled())
                    .build();

            // Add the service data to the model
            model.addAttribute("service", serviceEditRequest);
        } catch (AppException e) {
            // Handle case when the service is not found
            model.addAttribute("errorMessage", "Service not found: " + e.getMessage());
        }

        return "editService";  // Show the edit form with service data pre-filled
    }
    @PostMapping("/edit-service")
    public String editService(@ModelAttribute ServiceEditRequest request, Model model) {
        try {
            // Attempt to update the service
            petServiceService.updateService(request);
        } catch (AppException e) {
            // If an error occurs, display the error message
            model.addAttribute("errorMessage", "Error while editing service: " + e.getMessage());

            // Fetch and return the current service data to repopulate the form
            com.group.pet_service.model.PetService existingService = petServiceService.getServiceById(request.getId());
            model.addAttribute("service", ServiceEditRequest.builder()
                    .id(existingService.getId())
                    .name(existingService.getName())
                    .description(existingService.getDescription())
                    .price(existingService.getPrice())
                    .disabled(existingService.isDisabled())
                    .build());

            return "editService";  // Stay on the same page with the error message
        }

        return "redirect:/admin";  // Redirect to the admin page after success
    }

    @PostMapping("/delete-staff/{id}")
    public String deleteStaff(@PathVariable String id, Model model) {
        try {
            userService.deleteStaff(id);
        } catch (AppException e) {
            // Pass an error message to the frontend
            model.addAttribute("errorMessage", "Error while deleting staff: " + e.getMessage());
            return "admin";  // Return to admin page with an error message
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


    @GetMapping("/edit-species/{id}")
    public String editSpecies(@PathVariable String id, Model model) {
        try {
            // Fetch the species data by ID
            Species existingSpecies = speciesService.getSpeciesById(id);

            // Map the Species entity to the SpeciesEditRequest DTO (if needed)
            SpeciesEditRequest speciesEditRequest = SpeciesEditRequest.builder()
                    .id(existingSpecies.getId())
                    .name(existingSpecies.getName())
                    .description(existingSpecies.getDescription())
                    .build();

            model.addAttribute("request", speciesEditRequest);
        } catch (AppException e) {
            model.addAttribute("errorMessage", "Species not found: " + e.getMessage());
        }

        return "editSpecies";  // Show the edit form with species data pre-filled
    }


    @PostMapping("/edit-species")
    public String editSpecies(@ModelAttribute SpeciesEditRequest request, Model model) {
        try {
            speciesService.updateSpecies(request);
        } catch (AppException e) {
            // Catch exceptions and pass an error message to the frontend
            model.addAttribute("errorMessage", "Error while editing species: " + e.getMessage());
            return "editSpecies";  // Redirect back to the edit form page
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete-species/{id}")
    public String deleteSpecies(@PathVariable String id, Model model) {
        try {
            speciesService.deleteSpecies(id);
        } catch (AppException e) {
            // Pass an error message to the frontend
            model.addAttribute("errorMessage", "Error while deleting species: " + e.getMessage());
            return "admin";  // Return to admin page with an error message
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete-service/{id}")
    public String deleteService(@PathVariable String id, Model model) {
        try {
            petServiceService.deleteService(id);
        } catch (AppException e) {
            // Pass an error message to the frontend
            model.addAttribute("errorMessage", "Error while deleting species: " + e.getMessage());
            return "admin";  // Return to admin page with an error message
        }
        return "redirect:/admin";
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

    @GetMapping("/edit-pet/{id}")
    public String editPet(@PathVariable String id, Model model) {
        try {
            // Fetch the pet data by ID
            Pet existingPet = petService.getPetById(id);

            // Map the Pet entity to the PetEditRequest DTO (if needed)
            PetEditRequest petEditRequest = PetEditRequest.builder()
                    .id(existingPet.getId())
                    .name(existingPet.getName())
                    .description(existingPet.getDescription())
                    .height(existingPet.getHeight())
                    .weight(existingPet.getWeight())
                    .userId(existingPet.getUser().getId())
                    .speciesId(existingPet.getSpecies().getId())
                    .build();

            // Add the DTO to the model for pre-filling the form
            model.addAttribute("petRequest", petEditRequest);
        } catch (AppException e) {
            // Add an error message to the model if the pet is not found
            model.addAttribute("errorMessage", "Pet not found: " + e.getMessage());
        }

        return "editPet";  // Return the edit form page with data pre-filled
    }


    @PostMapping("/edit-pet")
    public String editPet(@ModelAttribute PetEditRequest request, Model model) {
        try {
            // Attempt to update the pet
            petService.updatePet(request);

            // Redirect to admin page on success
            return "redirect:/admin";
        } catch (AppException e) {
            // Add the error message
            model.addAttribute("errorMessage", "Error while editing pet: " + e.getMessage());

            // Retain the form data so the user doesn't lose input
            model.addAttribute("petRequest", request);

            // Return the editPet page with the error message
            return "editPet";
        }
    }


    @PostMapping("/delete-pet/{id}")
    public String deletePet(@PathVariable String id, Model model) {
        try {
            petService.deletePet(id);
        } catch (AppException e) {
            // Pass an error message to the frontend
            model.addAttribute("errorMessage", "Error while deleting pet: " + e.getMessage());
            return "admin";  // Return to the admin page with an error message
        }
        return "redirect:/admin";
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


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Thêm một đối tượng trống để gắn dữ liệu từ form
        model.addAttribute("loginRequest", new AuthenticationRequest());
        return "login"; // Tên file HTML trong thư mục templates (login.html)
    }
    @PostMapping("/login")
    public String processLogin(
            @ModelAttribute AuthenticationRequest loginRequest,
            HttpSession session,
            Model model) {
        try {
            // Gọi service để kiểm tra thông tin đăng nhập
            AuthenticationResponse authentication = authenticationService.login(loginRequest);

            // Lưu thông tin người dùng vào session
            session.setAttribute("loggedInUser", authentication);

            // Chuyển hướng đến trang admin nếu đăng nhập thành công
            return "redirect:/admin";
        } catch (AppException e) {
            // Nếu có lỗi, thêm thông báo lỗi vào model và quay lại form đăng nhập
            model.addAttribute("errorMessage", "Invalid username or password!");
            return "login"; // Quay lại form đăng nhập
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear authentication and session
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        // Optional: Redirect to home or login page
        return "redirect:/login";  // Redirect to the login page or home page
    }
}