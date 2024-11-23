package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.UserRegistrationDTO;
import com.eecs4413final.demo.dto.UserResponseDTO;
import com.eecs4413final.demo.model.User;
import com.eecs4413final.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Validated @RequestBody UserRegistrationDTO registrationDto) {
        User registeredUser = userService.registerUser(registrationDto);

        // Convert User entity to UserResponseDto to avoid exposing sensitive data
        UserResponseDTO responseDto = new UserResponseDTO(
                registeredUser.getUserId(),
                registeredUser.getUsername(),
                registeredUser.getEmail(),
                registeredUser.getPhone(),
                registeredUser.getRole(),
                registeredUser.getCreatedAt()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // Other user-related endpoints can be added here
}