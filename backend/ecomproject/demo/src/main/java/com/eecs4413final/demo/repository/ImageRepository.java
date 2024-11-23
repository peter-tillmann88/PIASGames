package com.eecs4413final.demo.repository;

import com.eecs4413final.demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByFileName(String fileName);
    List<Image> findByProduct_ProductId(Long productId);
    boolean existsByFileName(String fileName);
}