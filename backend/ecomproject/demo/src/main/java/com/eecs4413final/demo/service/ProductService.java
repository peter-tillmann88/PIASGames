package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.ProductDTO;
import com.eecs4413final.demo.model.Categories;
import com.eecs4413final.demo.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getByCategory(String category);
    List<Product> getByPlatform(String platform);
    List<Product> getByDeveloper(String developer);
    List<Product> getByDeveloperAndPlatform(String developer, String platform);
    List<Product> getByCategoryAndPlatform(String category, String platform);
    Product getByName(String name);
    List<Product> getByPlatformAndName(String platform, String name);
    public List<Product> getByCategoryListIn(Set<Long> categoryIds);

    Product getById(Long productId);
    void deleteById(Long productId);

    // Original addProduct
    Product addProduct(ProductDTO product);

    // New addProduct that also handles images
    Product addProduct(ProductDTO productDTO, List<MultipartFile> images) throws Exception;
    List<Product> searchProductsByName(String query);
}