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
        // Create new product instance
        Product newProduct = new Product();
        newProduct.setName(productDTO.getName());
        newProduct.setDeveloper(productDTO.getDeveloper());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setStock(productDTO.getStock());
        newProduct.setSaleMod(productDTO.getSaleMod());
        newProduct.setPlatform(productDTO.getPlatform());

        // Handle categories
        Set<Categories> categories = new HashSet<>();
        for (Long categoryId : productDTO.getCategoryIds()) {
            if (categoryId == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            Categories existingCategory = categoriesRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found for ID: " + categoryId));
            categories.add(existingCategory);
        }
        newProduct.setCategoryList(categories);

        // Save product to generate product ID
        Product savedProduct = productRepository.save(newProduct);
        System.out.println("Product saved with ID: " + savedProduct.getProductId());
        return savedProduct;
    }

    @Override
    public Product addProduct(ProductDTO productDTO, List<MultipartFile> images) throws Exception {
        // Create product first
        Product product = addProduct(productDTO);

        // If images are provided, upload them and associate with product
        if (images != null && !images.isEmpty()) {
            System.out.println("Uploading images for product ID: " + product.getProductId());
            imageService.addImages(product.getProductId(), images);
            // Reload product to get updated images
            product = productRepository.findById(product.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found after image upload"));
            System.out.println("Images associated with product ID: " + product.getProductId());
        } else {
            System.out.println("No images provided for product ID: " + product.getProductId());
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
        Product product = productRepository.findByName(name);
        if (product != null) {
            return product;
        } else {
            throw new ProductNotFoundException("Product not found with name: " + name);
        }
    }

    @Override
    public List<Product> getByPlatformAndName(String platform, String name) {
        return productRepository.findByPlatformAndName(platform, name);
    }

    @Override
    public List<Product> getByCategoryListIn(Set<Long> categoryIds) {
        Set<Categories> categories = new HashSet<>();
        for (Long categoryId : categoryIds) {
            Categories category = categoriesRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found for ID: " + categoryId));
            categories.add(category);
        }
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
            List<Image> productImages = product.getImages();
            // Use the imageService to delete images so that files on Supabase are also deleted
            for (Image image: productImages) {
                imageService.deleteById(image.getId()); // This triggers deletion from Supabase
            }
            // Now delete the product
            productRepository.deleteById(productId);
        } else {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
    }

    @Override
    public List<Product> searchProductsByName(String query) {
        // Use the correctly defined repository method
        return productRepository.findByNameContainingIgnoreCase(query);
    }
}