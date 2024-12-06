package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.ImageDTO;
import com.eecs4413final.demo.dto.ProductDTO;
import com.eecs4413final.demo.exception.ProductNotFoundException;
import com.eecs4413final.demo.model.Categories;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.repository.CategoriesRepository;
import com.eecs4413final.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoriesRepository categoriesRepository;
    private final ImageService imageService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoriesRepository categoriesRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.categoriesRepository = categoriesRepository;
        this.imageService = imageService;
    }

    @Override
    @Transactional
    public Product addProduct(ProductDTO productDTO, List<MultipartFile> imageFiles) throws Exception {
        Set<Categories> prodCategories = productDTO.getCategory();

        Set<Categories> repoCategories = new HashSet<>(categoriesRepository.findAll());

        // Check if category for product exists in repo, if not add
        for (Categories categories : prodCategories) {
            if (!repoCategories.contains(categories)) {
                categoriesRepository.save(categories);
            }
        }

        // Create the Product entity
        Product product = new Product(productDTO.getName(), productDTO.getDeveloper(), productDTO.getDescription(),
                productDTO.getPrice(), productDTO.getStock(), productDTO.getSaleMod(), prodCategories, productDTO.getPlatform());

        // Save the product first to get the productId
        Product savedProduct = productRepository.save(product);

        // Upload images and associate with product
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                ImageDTO imageDTO = imageService.addImage(savedProduct.getProductId(), file);
                // Optionally, you can collect imageDTOs
            }
        }

        return savedProduct;
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
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    @Override
    @Transactional
    public void deleteById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        // Images will be deleted automatically due to orphanRemoval = true
        productRepository.deleteById(productId);
    }

    @Override
    public List<ImageDTO> addImages(Long productId, List<MultipartFile> files) throws Exception {
        return imageService.addImages(productId, files);
    }

    @Override
    public void deleteImage(Long imageId) {
        imageService.deleteById(imageId);
    }
}