package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.ChangePasswordDTO;
import com.eecs4413final.demo.dto.UserRegistrationDTO;
import com.eecs4413final.demo.dto.UserResponseDTO;
import com.eecs4413final.demo.dto.UserUpdateDTO;



import com.eecs4413final.demo.exception.EmailAlreadyExistsException;
import com.eecs4413final.demo.exception.UsernameAlreadyExistsException;
import com.eecs4413final.demo.model.ShoppingCart;
import com.eecs4413final.demo.model.User;
import com.eecs4413final.demo.service.ShoppingCartService;
import com.eecs4413final.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, ShoppingCartService shoppingCartService) {
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
            @RequestParam String username,
            @Valid @RequestBody UserUpdateDTO updateDto) {
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
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String username,
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

    @GetMapping("/wishlist")
    public ResponseEntity<List<?>> getWishlist(@RequestParam String username) {
        // Placeholder for wishlist retrieval logic
        // Replace '?' with your actual WishlistItemDTO or appropriate class once implemented
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}