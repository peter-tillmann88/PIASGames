package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.ChangePasswordDTO;
import com.eecs4413final.demo.dto.UserRegistrationDTO;
import com.eecs4413final.demo.dto.UserResponseDTO;
import com.eecs4413final.demo.dto.UserUpdateDTO;

import com.eecs4413final.demo.exception.EmailAlreadyExistsException;
import com.eecs4413final.demo.exception.UsernameAlreadyExistsException;
import com.eecs4413final.demo.model.User;

import com.eecs4413final.demo.service.UserService;

import com.eecs4413final.demo.util.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService,  JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDto) {
        User registeredUser = userService.registerUser(registrationDto);
        UserResponseDTO responseDto = new UserResponseDTO(
                registeredUser.getUserId(),
                registeredUser.getUsername(),
                registeredUser.getEmail(),
                registeredUser.getPhone(),
                registeredUser.getRole(),
                registeredUser.getCreatedAt(),
                registeredUser.getCreditCard(),
                registeredUser.getExpiryDate(),
                registeredUser.getCountry(),
                registeredUser.getProvince(),
                registeredUser.getAddress(),
                registeredUser.getPostalCode()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.extractUsername(token);
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserResponseDTO responseDto = new UserResponseDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole(),
                    user.getCreatedAt(),
                    user.getCreditCard(),
                    user.getExpiryDate(),
                    user.getCountry(),
                    user.getProvince(),
                    user.getAddress(),
                    user.getPostalCode()
            );
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UserUpdateDTO updateDto) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.extractUsername(token);

        try {
            User updatedUser = userService.updateUserProfile(username, updateDto);
            UserResponseDTO responseDto = new UserResponseDTO(
                    updatedUser.getUserId(),
                    updatedUser.getUsername(),
                    updatedUser.getEmail(),
                    updatedUser.getPhone(),
                    updatedUser.getRole(),
                    updatedUser.getCreatedAt(),
                    updatedUser.getCreditCard(),
                    updatedUser.getExpiryDate(),
                    updatedUser.getCountry(),
                    updatedUser.getProvince(),
                    updatedUser.getAddress(),
                    updatedUser.getPostalCode()
            );
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            LoggerFactory.getLogger(UserController.class).error("Error in getUserProfile: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.extractUsername(token);

        try {
            boolean isChanged = userService.changePassword(username, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
            if (isChanged) {
                return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid old password", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error changing password: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserResponseDTO> userResponseList = users.stream().map(user -> new UserResponseDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole(),
                    user.getCreatedAt(),
                    user.getCreditCard(),
                    user.getExpiryDate(),
                    user.getCountry(),
                    user.getProvince(),
                    user.getAddress(),
                    user.getPostalCode()
            )).toList();

            return new ResponseEntity<>(userResponseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteUserAccount(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.extractUsername(token);

        try {
            userService.deleteUserByUsername(username);
            return new ResponseEntity<>("User account deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}