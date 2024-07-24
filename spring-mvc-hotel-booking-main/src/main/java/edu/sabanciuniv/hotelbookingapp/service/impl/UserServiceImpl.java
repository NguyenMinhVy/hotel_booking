package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.exception.UsernameAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.*;
import edu.sabanciuniv.hotelbookingapp.model.dto.ResetPasswordDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.model.enums.RoleType;
import edu.sabanciuniv.hotelbookingapp.repository.CustomerRepository;
import edu.sabanciuniv.hotelbookingapp.repository.HotelManagerRepository;
import edu.sabanciuniv.hotelbookingapp.repository.RoleRepository;
import edu.sabanciuniv.hotelbookingapp.repository.UserRepository;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final HotelManagerRepository hotelManagerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Users saveUser(UserRegistrationDTO registrationDTO) {
        log.info("Attempting to save a new user: {}", registrationDTO.getUsername());

        Optional<Users> existingUser = Optional.ofNullable(userRepository.findByUsername(registrationDTO.getUsername()));
        if (existingUser.isPresent()) {
            throw new UsernameAlreadyExistsException("This username is already registered!");
        }

        Users users = mapRegistrationDtoToUser(registrationDTO);

        if (RoleType.CUSTOMER.equals(registrationDTO.getRoleType())) {
            Customer customer = Customer.builder().users(users).build();
            customerRepository.save(customer);
        } else if (RoleType.HOTEL_MANAGER.equals(registrationDTO.getRoleType())) {
            HotelManager hotelManager = HotelManager.builder().users(users).build();
            hotelManagerRepository.save(hotelManager);
        }

        Users savedUsers = userRepository.save(users);
        log.info("Successfully saved new user: {}", registrationDTO.getUsername());
        return savedUsers;
    }

    @Override
    public Users findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDTO findUserDTOByUsername(String username) {
        Optional<Users> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
        Users users = userOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return mapUserToUserDto(users);
    }

    @Override
    public UserDTO findUserById(Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        return mapUserToUserDto(users);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<Users> usersList = userRepository.findAll();

        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users users : usersList) {
            UserDTO userDTO = mapUserToUserDto(users);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO) {
        log.info("Attempting to update user with ID: {}", userDTO.getId());

        Users users = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (usernameExistsAndNotSameUser(userDTO.getUsername(), users.getId())) {
            throw new UsernameAlreadyExistsException("This username is already registered!");
        }

        setFormattedDataToUser(users, userDTO);
        userRepository.save(users);
        log.info("Successfully updated existing user with ID: {}", userDTO.getId());
    }

    @Override
    @Transactional
    public void updateLoggedInUser(UserDTO userDTO) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Users loggedInUsers = userRepository.findByUsername(loggedInUsername);
        log.info("Attempting to update logged in user with ID: {}", loggedInUsers.getId());

        if (usernameExistsAndNotSameUser(userDTO.getUsername(), loggedInUsers.getId())) {
            throw new UsernameAlreadyExistsException("This username is already registered!");
        }

        setFormattedDataToUser(loggedInUsers, userDTO);
        userRepository.save(loggedInUsers);
        log.info("Successfully updated logged in user with ID: {}", loggedInUsers.getId());

        // Create new authentication token
        updateAuthentication(userDTO);
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("Attempting to delete user with ID: {}", id);
        userRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
    }

    // TODO: 23.07.2023
    @Override
    public Users resetPassword(ResetPasswordDTO resetPasswordDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        Users users = userRepository.findByUsername(loggedInUsername);
        if (users == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(resetPasswordDTO.getOldPassword(), users.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        users.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        return userRepository.save(users);
    }

    private Users mapRegistrationDtoToUser(UserRegistrationDTO registrationDTO) {
        Role userRole = roleRepository.findByRoleType(registrationDTO.getRoleType());
        return Users.builder()
                .username(registrationDTO.getUsername().trim())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .name(formatText(registrationDTO.getName()))
                .lastName(formatText(registrationDTO.getLastName()))
                .role(userRole)
                .build();
    }

    private UserDTO mapUserToUserDto(Users users) {
        return UserDTO.builder()
                .id(users.getId())
                .username(users.getUsername())
                .name(users.getName())
                .lastName(users.getLastName())
                .role(users.getRole())
                .build();
    }

    private boolean usernameExistsAndNotSameUser(String username, Long userId) {
        Optional<Users> existingUserWithSameUsername = Optional.ofNullable(userRepository.findByUsername(username));
        return existingUserWithSameUsername.isPresent() && !existingUserWithSameUsername.get().getId().equals(userId);
    }

    private String formatText(String text) {
        return StringUtils.capitalize(text.trim());
    }

    private void setFormattedDataToUser(Users users, UserDTO userDTO) {
        users.setUsername(userDTO.getUsername());
        users.setName(formatText(userDTO.getName()));
        users.setLastName(formatText(userDTO.getLastName()));
    }

    // In production applications, prefer logging out the user and requiring re-login over the method below.
    // It updates the authentication context directly, which could be a potential security risk.
    private void updateAuthentication(UserDTO userDTO) {
        Users users = userRepository.findByUsername(userDTO.getUsername());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + users.getRole().getRoleType().name()));

        UserDetails newUserDetails = new org.springframework.security.core.userdetails.User(
                users.getUsername(),
                users.getPassword(),
                authorities
        );

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
                newUserDetails,
                null,
                newUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

}
