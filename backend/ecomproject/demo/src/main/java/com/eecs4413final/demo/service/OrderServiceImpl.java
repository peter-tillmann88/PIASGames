package com.eecs4413final.demo.service;

import com.eecs4413final.demo.exception.ShoppingCartNotFoundException;
import com.eecs4413final.demo.model.*;
import com.eecs4413final.demo.repository.OrderItemRepository;
import com.eecs4413final.demo.repository.OrderRepository;
import com.eecs4413final.demo.repository.ProductRepository;
import com.eecs4413final.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            ShoppingCartService shoppingCartService,
                            ProductRepository productRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Order createOrderFromCart(Long userId) {
        ShoppingCart cart = shoppingCartService.getCartByUser(userId);
        if (cart == null || cart.getShoppingCartItems().isEmpty()) {
            throw new ShoppingCartNotFoundException("Shopping cart is empty for user ID " + userId);
        }

        double totalAmount = cart.getShoppingCartItems().stream()
                .map(item -> item.getTotalPrice().doubleValue())
                .reduce(0.0, Double::sum);

        Order newOrder = new Order();
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                throw new IllegalArgumentException("User with ID " + userId + " not found.");
            }
            User user = userOpt.get();

            newOrder.setUserId(userId.intValue());
            newOrder.setTotalAmount(totalAmount);
            newOrder.setStatus("COMPLETE");
            newOrder.setOrderDate(LocalDateTime.now());
            newOrder = orderRepository.save(newOrder);
            logger.info("Order created successfully: {}", newOrder);
        } catch (Exception e) {
            logger.error("Error adding order for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }

        try {
            for (ShoppingCartItems cartItem : cart.getShoppingCartItems()) {
                Product product = cartItem.getProduct();

                
                int newStock = product.getStock() - cartItem.getQuantity();
                product.setStock(newStock);
                productRepository.save(product);

                
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(newOrder);
                orderItem.setProduct(product);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPriceAtPurchase(cartItem.getUnitPrice());

                
                newOrder.addOrderItem(orderItem);

                orderItemRepository.save(orderItem);
            }
            logger.info("OrderItems created successfully for order ID {}", newOrder.getOrderID());
        } catch (Exception e) {
            logger.error("Error converting ShoppingCartItem to OrderItem for order ID {}: {}", newOrder.getOrderID(), e.getMessage(), e);
            throw e;
        }
        
        shoppingCartService.clearCart(cart.getCartId());

       
        return newOrder;
    }

    @Override
    public List<Order> getOrdersForUser(Long userId) {
        logger.info("Fetching orders for user ID: {}", userId);
        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId.intValue());
        logger.info("Found {} orders for user ID: {}", orders.size(), userId);
        return orders;
    }

    @Override
    public List<Order> getAllOrders() {
        logger.info("Admin fetching all orders.");
        List<Order> orders = orderRepository.findAllByOrderByOrderDateDesc();
        logger.info("Total orders fetched: {}", orders.size());
        return orders;
    }
}
