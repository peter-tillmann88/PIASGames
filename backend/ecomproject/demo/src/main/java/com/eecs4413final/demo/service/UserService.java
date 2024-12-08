package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.UserRegistrationDTO;
import com.eecs4413final.demo.model.User;
import java.util.Optional;

public interface UserService {

    User registerUser(UserRegistrationDTO registrationDto);
    Optional<User> findByUsername(String username); // New method
    Optional<User> authenticateUser(String username, String password);
    boolean changePassword(String username, String oldPassword, String newPassword);
    void deleteUserByUsername(String username);





    // Other user-related methods can be added here
}