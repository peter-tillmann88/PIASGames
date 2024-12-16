package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.OrderDTO;
import com.eecs4413final.demo.mapper.OrderMapper;
import com.eecs4413final.demo.model.Order;
import com.eecs4413final.demo.model.User;
import com.eecs4413final.demo.repository.UserRepository;
import com.eecs4413final.demo.service.OrderService;
import com.eecs4413final.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public OrderController(OrderService orderService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        Long userId = jwtUtil.extractUserId(token);
        try {
            Order order = orderService.createOrderFromCart(userId);
            OrderDTO orderDTO = OrderMapper.toDTO(order);
            return ResponseEntity.ok(orderDTO);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not complete checkout: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getOrderHistory(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        Long userId = jwtUtil.extractUserId(token);
        try {
            List<Order> orders = orderService.getOrdersForUser(userId);
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not retrieve order history: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // New endpoint to get all orders (Admin only)
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        Long userId = jwtUtil.extractUserId(token);

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        }

        User user = userOpt.get();
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            return new ResponseEntity<>("Access denied: Admins only", HttpStatus.FORBIDDEN);
        }

        try {
            List<Order> orders = orderService.getAllOrders();
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not retrieve orders: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
