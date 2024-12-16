package com.eecs4413final.demo.repository;

import com.eecs4413final.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Long customerId);
    List<Order> findByUserIdOrderByOrderDateDesc(int intValue);

    List<Order> findAllByOrderByOrderDateDesc();
}