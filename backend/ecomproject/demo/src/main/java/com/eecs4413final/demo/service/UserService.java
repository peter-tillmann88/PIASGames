package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.UserRegistrationDTO;
import com.eecs4413final.demo.model.User;

public interface UserService {

    User registerUser(UserRegistrationDTO registrationDto);

    // Other user-related methods can be added here
}