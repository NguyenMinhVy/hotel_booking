package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.Users;
import edu.sabanciuniv.hotelbookingapp.model.dto.ResetPasswordDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;

import java.util.List;

public interface UserService {

    Users saveUser(UserRegistrationDTO registrationDTO);

    // For registration
    Users findUserByUsername(String username);

    UserDTO findUserDTOByUsername(String username);

    UserDTO findUserById(Long id);

    List<UserDTO> findAllUsers();

    void updateUser(UserDTO userDTO);

    void updateLoggedInUser(UserDTO userDTO);

    void deleteUserById(Long id);

    Users resetPassword(ResetPasswordDTO resetPasswordDTO);

}
