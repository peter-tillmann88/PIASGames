package com.eecs4413final.demo.mapper;

import com.eecs4413final.demo.dto.ShoppingCartResponseDTO;
import com.eecs4413final.demo.dto.ShoppingCartResponseDTO.CartItemDTO;
import com.eecs4413final.demo.model.ShoppingCart;

import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartMapper {

    public static ShoppingCartResponseDTO toResponseDTO(ShoppingCart cart, String message) {
        List<CartItemDTO> items = cart.getShoppingCartItems().stream()
                .map(item -> new CartItemDTO(
                        item.getCartItemId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice().doubleValue(),
                        item.getTotalPrice().doubleValue()
                ))
                .collect(Collectors.toList());

        return new ShoppingCartResponseDTO(
                cart.getCartId(),
                cart.getCustomerId(),
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                items,
                message
        );
    }
}