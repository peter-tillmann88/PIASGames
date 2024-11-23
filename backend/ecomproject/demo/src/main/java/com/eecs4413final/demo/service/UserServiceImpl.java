package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.UserRegistrationDTO;
import com.eecs4413final.demo.exception.EmailAlreadyExistsException;
import com.eecs4413final.demo.exception.UsernameAlreadyExistsException;
import com.eecs4413final.demo.model.User;
import com.eecs4413final.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDTO registrationDto) {
        // Check if username already exists
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username '" + registrationDto.getUsername() + "' is already taken.");
        }

        // Check if email already exists
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email '" + registrationDto.getEmail() + "' is already registered.");
        }

        // Create new User entity
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setPhone(registrationDto.getPhone());
        user.setRole(User.Role.CUSTOMER); // Default role

        // Save user to the database
        return userRepository.save(user);
    }

    // Implement other methods as needed
}
