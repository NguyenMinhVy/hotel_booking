package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.exception.UsernameAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelDTO;
import edu.sabanciuniv.hotelbookingapp.model.enums.RoleType;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.security.RedirectUtil;
import edu.sabanciuniv.hotelbookingapp.service.HotelService;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @Autowired
    private  UserService userService;

    @Autowired
    private HotelService hotelService;

    @GetMapping("/")
    public String homePage(Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null) return redirect;
        log.debug("Accessing home page");
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<HotelDTO> hotelDTOList = hotelService.findAllHotels();
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(1);
        model.addAttribute("hotels", hotelDTOList);
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);
//        String redirect = getAuthenticatedUserRedirectUrl(authentication);
//        if (redirect != null) return redirect;
        log.debug("Accessing home page");
        return "indexAfterLogin";
    }

    @GetMapping("/login")
    public String loginPage(Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null) return redirect;
        log.debug("Accessing login page");
        return "login";
    }

    @GetMapping("/register/customer")
    public String showCustomerRegistrationForm(@ModelAttribute("users") UserRegistrationDTO registrationDTO, Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null) return redirect;
        log.info("Showing customer registration form");
        return "register-customer";
    }

    @PostMapping("/register/customer")
    public String registerCustomerAccount(@Valid @ModelAttribute("users") UserRegistrationDTO registrationDTO, BindingResult result) {
        log.info("Attempting to register customer account: {}", registrationDTO.getUsername());
        registrationDTO.setRoleType(RoleType.CUSTOMER);
        return registerUser(registrationDTO, result, "register-customer", "register/customer");
    }

    @GetMapping("/register/manager")
    public String showManagerRegistrationForm(@ModelAttribute("users") UserRegistrationDTO registrationDTO, Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null) return redirect;
        log.info("Showing manager registration form");
        return "register-manager";
    }

    @PostMapping("/register/manager")
    public String registerManagerAccount(@Valid @ModelAttribute("users") UserRegistrationDTO registrationDTO, BindingResult result) {
        log.info("Attempting to register manager account: {}", registrationDTO.getUsername());
        registrationDTO.setRoleType(RoleType.HOTEL_MANAGER);
        return registerUser(registrationDTO, result, "register-manager", "register/manager");
    }

    private String registerUser(UserRegistrationDTO registrationDTO, BindingResult result, String view, String redirectUrl) {
        if (result.hasErrors()) {
            log.warn("Registration failed due to validation errors: {}", result.getAllErrors());
            return view;
        }
        try {
            userService.saveUser(registrationDTO);
            log.info("User registration successful: {}", registrationDTO.getUsername());
        } catch (UsernameAlreadyExistsException e) {
            log.error("Registration failed due to username already exists: {}", e.getMessage());
            result.rejectValue("username", "user.exists", e.getMessage());
            return view;
        }
        return "redirect:/" + redirectUrl + "?success";
    }

    private String getAuthenticatedUserRedirectUrl(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String redirectUrl = RedirectUtil.getRedirectUrl(authentication);
            if (redirectUrl != null) {
                return "redirect:" + redirectUrl;
            }
        }
        return null;
    }

}

