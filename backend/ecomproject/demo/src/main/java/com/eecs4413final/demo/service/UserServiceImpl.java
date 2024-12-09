package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.UserRegistrationDTO;
import com.eecs4413final.demo.dto.UserUpdateDTO;
import com.eecs4413final.demo.exception.EmailAlreadyExistsException;
import com.eecs4413final.demo.exception.UsernameAlreadyExistsException;
import com.eecs4413final.demo.model.User;
import com.eecs4413final.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDTO registrationDto) {
        logger.info("Registering user: {}", registrationDto.getUsername());

        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            logger.warn("Username '{}' is already taken.", registrationDto.getUsername());
            throw new UsernameAlreadyExistsException("Username '" + registrationDto.getUsername() + "' is already taken.");
        }

        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            logger.warn("Email '{}' is already registered.", registrationDto.getEmail());
            throw new EmailAlreadyExistsException("Email '" + registrationDto.getEmail() + "' is already registered.");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setPhone(registrationDto.getPhone());
        user.setRole(User.Role.CUSTOMER);
        user.setCreditCard(registrationDto.getCreditCard());
        user.setExpiryDate(registrationDto.getExpiryDate());
        user.setCountry(registrationDto.getCountry());
        user.setProvince(registrationDto.getProvince());
        user.setAddress(registrationDto.getAddress());
        user.setPostalCode(registrationDto.getPostalCode());

        try {
            User savedUser = userRepository.save(user);
            logger.info("User '{}' registered successfully with ID {}", savedUser.getUsername(), savedUser.getUserId());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error saving user '{}': {}", registrationDto.getUsername(), e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> authenticateUser(String username, String password) {
        logger.info("Authenticating user: {}", username);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            logger.debug("User '{}' found. Verifying password.", username);
            boolean isPasswordMatch = passwordEncoder.matches(password, user.getPasswordHash());
            logger.debug("Password match for user '{}': {}", username, isPasswordMatch);
            if (isPasswordMatch) {
                logger.info("User '{}' authenticated successfully.", username);
                return Optional.of(user);
            } else {
                logger.warn("Invalid password for user '{}'.", username);
            }
        } else {
            logger.warn("User '{}' not found.", username);
        }
        return Optional.empty();
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        logger.info("Attempting to change password for user: {}", username);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                String hashedNewPassword = passwordEncoder.encode(newPassword);
                user.setPasswordHash(hashedNewPassword);
                userRepository.save(user);
                logger.info("Password successfully changed for user: {}", username);
                return true;
            } else {
                logger.warn("Old password does not match for user: {}", username);
                return false;
            }
        } else {
            logger.error("User '{}' not found for password change request.", username);
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void deleteUserByUsername(String username) {
        logger.info("Attempting to delete user: {}", username);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            userRepository.delete(user);
            logger.info("User '{}' deleted successfully", username);
        } else {
            logger.error("User '{}' not found for deletion", username);
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public User updateUserProfile(String username, UserUpdateDTO updateDto) {
        logger.info("Updating profile for user: {}", username);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (updateDto.getUsername() != null && !updateDto.getUsername().equals(user.getUsername())) {
                if (userRepository.existsByUsername(updateDto.getUsername())) {
                    throw new UsernameAlreadyExistsException("Username '" + updateDto.getUsername() + "' is already taken.");
                }
                user.setUsername(updateDto.getUsername());
            }

            if (updateDto.getEmail() != null && !updateDto.getEmail().equals(user.getEmail())) {
                if (userRepository.existsByEmail(updateDto.getEmail())) {
                    throw new EmailAlreadyExistsException("Email '" + updateDto.getEmail() + "' is already registered.");
                }
                user.setEmail(updateDto.getEmail());
            }

            if (updateDto.getPhone() != null) {
                user.setPhone(updateDto.getPhone());
            }

            if (updateDto.getCreditCard() != null) {
                user.setCreditCard(updateDto.getCreditCard());
            }

            if (updateDto.getExpiryDate() != null) {
                user.setExpiryDate(updateDto.getExpiryDate());
            }

            if (updateDto.getAddress() != null) {
                user.setAddress(updateDto.getAddress());
            }

            if (updateDto.getPostalCode() != null) {
                user.setPostalCode(updateDto.getPostalCode());
            }

            if (updateDto.getCountry() != null) {
                user.setCountry(updateDto.getCountry());
            }

            if (updateDto.getProvince() != null) {
                user.setProvince(updateDto.getProvince());
            }

            try {
                User updatedUser = userRepository.save(user);
                logger.info("User '{}' profile updated successfully.", username);
                return updatedUser;
            } catch (Exception e) {
                logger.error("Error updating profile for user '{}': {}", username, e.getMessage());
                throw e;
            }
        } else {
            logger.error("User '{}' not found for profile update.", username);
            throw new IllegalArgumentException("User not found");
        }
    }
}
