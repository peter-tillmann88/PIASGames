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
    private final BlacklistService blacklistService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, BlacklistService blacklistService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequest) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());
        Optional<User> userOpt = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            try {
                Long userId = user.getUserId();
                String accessToken = jwtUtil.generateAccessToken(user.getUsername(), userId);
                String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), userId);

                logger.info("JWT tokens generated for user: {}", user.getUsername());

                Map<String, String> response = new HashMap<>();
                response.put("accessToken", accessToken);
                response.put("refreshToken", refreshToken);
                response.put("role", user.getRole());

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

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            blacklistService.blacklistToken(token);
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

        String refreshToken = authHeader.substring(7);
        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                logger.warn("Invalid refresh token provided");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            String username = jwtUtil.extractUsername(refreshToken);
            // Retrieve the user to get the userId
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            Long userId = userOpt.get().getUserId();
            String newAccessToken = jwtUtil.generateAccessToken(username, userId);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            logger.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("valid", false, "message", "Invalid token format"));
        }

        String token = authHeader.substring(7);
        try {
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                Optional<User> userOpt = userService.findByUsername(username);
                if (userOpt.isPresent()) {
                    Long userId = userOpt.get().getUserId();
                    return ResponseEntity.ok(Map.of("valid", true, "username", username, "userId", userId));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("valid", false, "message", "User not found"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token is invalid or expired"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("valid", false, "message", e.getMessage()));
        }
    }
}
