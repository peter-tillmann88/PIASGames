package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.LoginRequestDTO;
import com.eecs4413final.demo.model.User;
import com.eecs4413final.demo.service.BlacklistService;
import com.eecs4413final.demo.service.UserService;
import com.eecs4413final.demo.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BlacklistService blacklistService; // Correct type for BlacklistService
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, BlacklistService blacklistService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService; // Properly assign BlacklistService
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequest) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());
        Optional<User> userOpt = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            try {
                // Generate Access and Refresh Tokens
                String accessToken = jwtUtil.generateAccessToken(user.getUsername());
                String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

                logger.info("JWT tokens generated for user: {}", user.getUsername());

                // Return tokens along with user role
                Map<String, String> response = new HashMap<>();
                response.put("accessToken", accessToken);
                response.put("refreshToken", refreshToken);
                response.put("role", user.getRole()); // Include the user's role in the response

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                logger.error("Error generating JWT tokens for user '{}': {}", user.getUsername(), e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            logger.warn("Invalid login attempt for user: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token format");
        }

        String token = authHeader.substring(7); // Extract the JWT after "Bearer "
        try {
            String username = jwtUtil.extractUsername(token); // Extract username for debugging
            blacklistService.blacklistToken(token); // Add token to blacklist
            logger.info("Token blacklisted for user: {}", username);
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during logout");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String refreshToken = authHeader.substring(7); // Extract the JWT after "Bearer "
        try {
            // Validate Refresh Token
            if (!jwtUtil.validateToken(refreshToken)) {
                logger.warn("Invalid refresh token provided");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Extract Username from Refresh Token
            String username = jwtUtil.extractUsername(refreshToken);

            // Generate New Access Token
            String newAccessToken = jwtUtil.generateAccessToken(username);

            // Return the new access token
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", refreshToken); // Optionally return the same refresh token

            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            logger.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
