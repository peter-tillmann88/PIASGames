package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.ImageDTO;
import com.eecs4413final.demo.dto.ProductDTO;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint to add a new product with images.
     *
     * @param productDTO The product details as JSON.
     * @param images     The image files.
     * @return The created product.
     */
    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Product savedProduct = productService.addProduct(productDTO, images);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to add product. ProductDTO: {}, Exception: {}", productDTO, e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to add images to an existing product.
     *
     * @param productId The ID of the product.
     * @param images    The image files to add.
     * @return The list of added images.
     */
    @PostMapping("/{productId}/images")
    public ResponseEntity<List<ImageDTO>> addImagesToProduct(
            @PathVariable Long productId,
            @RequestParam("images") List<MultipartFile> images) {
        try {
            List<ImageDTO> addedImages = productService.addImages(productId, images);
            return new ResponseEntity<>(addedImages, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to delete an image from a product.
     *
     * @param imageId The ID of the image to delete.
     * @return HTTP status indicating the outcome.
     */
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        try {
            productService.deleteImage(imageId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Additional endpoints (e.g., get, update, delete products) can be added here
}