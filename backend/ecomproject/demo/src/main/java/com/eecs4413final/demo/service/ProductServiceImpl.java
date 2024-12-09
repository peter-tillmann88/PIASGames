package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.ProductDTO;
import com.eecs4413final.demo.exception.CategoryNotFoundException;
import com.eecs4413final.demo.exception.ProductNotFoundException;
import com.eecs4413final.demo.model.Categories;
import com.eecs4413final.demo.model.Image;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.repository.CategoriesRepository;
import com.eecs4413final.demo.repository.ImageRepository;
import com.eecs4413final.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoriesRepository categoriesRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoriesRepository categoriesRepository,
                              ImageService imageService,
                              ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.categoriesRepository = categoriesRepository;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    @Override
    public Product addProduct(ProductDTO productDTO) {
        // Original method without images
        Product newProduct = new Product();
        newProduct.setName(productDTO.getName());
        newProduct.setDeveloper(productDTO.getDeveloper());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setStock(productDTO.getStock());
        newProduct.setSaleMod(productDTO.getSaleMod());
        newProduct.setPlatform(productDTO.getPlatform());

        Set<Categories> categories = new HashSet<>();
        for (Categories category : productDTO.getCategories()) {
            if (category.getCategoryId() == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            Categories existingCategory = categoriesRepository.findById(category.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found for ID: " + category.getCategoryId()));
            categories.add(existingCategory);
        }
        newProduct.setCategoryList(categories);

        return productRepository.save(newProduct);
    }

    @Override
    public Product addProduct(ProductDTO productDTO, List<MultipartFile> images) throws Exception {
        // Create product first
        Product product = addProduct(productDTO);

        // If images are provided, upload them and associate with product
        if (images != null && !images.isEmpty()) {
            imageService.addImages(product.getProductId(), images);
            // Reload product to get updated images
            product = productRepository.findById(product.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found after image upload"));
        }

        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getByCategory(String category) {
        return productRepository.findByCategoryList_Name(category);
    }

    @Override
    public List<Product> getByPlatform(String platform) {
        return productRepository.findByPlatform(platform);
    }

    @Override
    public List<Product> getByDeveloper(String developer) {
        return productRepository.findByDeveloper(developer);
    }

    @Override
    public List<Product> getByDeveloperAndPlatform(String developer, String platform) {
        return productRepository.findByDeveloperAndPlatform(developer, platform);
    }

    @Override
    public List<Product> getByCategoryAndPlatform(String category, String platform) {
        return productRepository.findByCategoryList_NameAndPlatform(category, platform);
    }

    @Override
    public Product getByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getByPlatformAndName(String platform, String name) {
        return productRepository.findByPlatformAndName(platform, name);
    }

    @Override
    public List<Product> getByCategoryListIn(Set<Categories> categories) {
        return productRepository.findByCategoryListIn(categories);
    }

    @Override
    public Product getById(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            return product;
        } else {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
    }

    @Override
    public void deleteById(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            productRepository.deleteById(productId);
        } else {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
    }
}