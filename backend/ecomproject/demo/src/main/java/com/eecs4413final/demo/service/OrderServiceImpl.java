// src/main/java/com/eecs4413final/demo/service/OrderServiceImpl.java

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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
    public Order createOrderFromCart(Long userId) {
        // 1. Get the user's cart
        ShoppingCart cart = shoppingCartService.getCartByUser(userId);

        // 2. Calculate the total amount from the cart items
        double totalAmount = cart.getShoppingCartItems().stream()
                .map(item -> item.getTotalPrice().doubleValue())
                .reduce(0.0, Double::sum);

        // 3. Create a new Order entity
        Order newOrder = new Order();
        try{
            Optional<User> user = userRepository.findById(userId);
            newOrder.setUserId(userId.intValue()); // assuming customerID is an int, cast userId
            newOrder.setTotalAmount(totalAmount);
            newOrder.setStatus("COMPLETE");
            newOrder.setOrderDate(LocalDateTime.now());
            newOrder = orderRepository.save(newOrder);
            logger.info("We have created the order");
        }
        catch (Exception e){
            logger.error("Error adding order: {}", e.getMessage());
        }


        // 4. Convert each ShoppingCartItem into an OrderItem and reduce product stock
        try{
            for (ShoppingCartItems cartItem : cart.getShoppingCartItems()) {
                Product product = cartItem.getProduct();

                // Decrement product stock
                int newStock = product.getStock() - cartItem.getQuantity();
                product.setStock(newStock);
                productRepository.save(product);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(newOrder);
                orderItem.setProduct(product);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPriceAtPurchase(cartItem.getUnitPrice()); // unit price at time of purchase
                orderItemRepository.save(orderItem);
            }
            logger.info("We have created the orderItems");
        }
        catch (Exception e){
            logger.error("Error converting shoppingcartitem to an order item: {}", e.getMessage());
        }


        // 5. Clear the cart
        shoppingCartService.clearCart(cart.getCartId());

        // Return the newly created order
        return newOrder;
    }
}