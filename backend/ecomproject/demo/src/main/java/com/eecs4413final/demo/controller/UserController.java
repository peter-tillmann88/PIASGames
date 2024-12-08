package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.ChangePasswordDTO;
import com.eecs4413final.demo.dto.UserRegistrationDTO;
import com.eecs4413final.demo.dto.UserResponseDTO;
import com.eecs4413final.demo.model.User;
import com.eecs4413final.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
                registeredUser.getCreatedAt()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(@RequestParam String username) {
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserResponseDTO responseDto = new UserResponseDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole(),
                    user.getCreatedAt()
            );
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String username, // Username as a query parameter
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            boolean isChanged = userService.changePassword(username, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
            if (isChanged) {
                return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid old password", HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error changing password: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteUserAccount(@RequestParam String username) {
        try {
            userService.deleteUserByUsername(username);
            return new ResponseEntity<>("User account deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("User not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
