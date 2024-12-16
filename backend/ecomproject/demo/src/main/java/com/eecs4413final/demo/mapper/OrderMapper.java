package com.eecs4413final.demo.mapper;

import com.eecs4413final.demo.dto.OrderDTO;
import com.eecs4413final.demo.dto.OrderItemDTO;
import com.eecs4413final.demo.model.Order;
import com.eecs4413final.demo.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toDTO(Order order) {
        List<OrderItemDTO> orderItems = order.getOrderItems().stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getOrderID(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                orderItems
        );
    }

    public static OrderItemDTO toDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getOrderItemID(),
                orderItem.getProduct().getProductId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getPriceAtPurchase()
        );
    }
}
