package com.eecs4413final.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RawCartService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RawCartService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean updateCartItemQuantity(Long cartItemId, int newQuantity) {
        String sql = "UPDATE shoppingcartitems SET quantity = ? WHERE cart_itemid = ?";
        int rowsAffected = jdbcTemplate.update(sql, newQuantity, cartItemId);
        return rowsAffected > 0; // Return true if the update was successful
    }

    public boolean deleteCartItem(Long cartItemId) {
        String sql = "DELETE FROM shoppingcartitems WHERE cart_itemid = ?";
        int rowsAffected = jdbcTemplate.update(sql, cartItemId);
        return rowsAffected > 0; // Return true if at least one row was deleted
    }

    public String getCartItemsByUserId(Long userId) {
        String sql = "SELECT json_agg(json_build_object(" +
                "'cartid', sci.cartid, " +
                "'cart_itemid', sci.cart_itemid, " +
                "'productid', sci.productid, " +
                "'product_name', p.name, " +
                "'quantity', sci.quantity, " +
                "'unit_price', sci.unit_price, " +
                "'total_price', sci.total_price, " +
                "'added_at', sci.added_at, " +
                "'image_name', i.file_name" +    // Add image_name to the JSON object
                ")) AS cart_items " +
                "FROM shoppingcartitems sci " +
                "JOIN shopping_carts sc ON sci.cartid = sc.cartid " +
                "JOIN products p ON sci.productid = p.productid " +
                // LATERAL JOIN to get the first associated image for each product
                "LEFT JOIN LATERAL ( " +
                "   SELECT file_name " +
                "   FROM image img " +
                "   WHERE img.productid = p.productid " +
                "   ORDER BY img.image_id " +
                "   LIMIT 1 " +
                ") i ON TRUE " +
                "WHERE sc.user_id = ?";

        return jdbcTemplate.queryForObject(sql, String.class, userId);
    }
}