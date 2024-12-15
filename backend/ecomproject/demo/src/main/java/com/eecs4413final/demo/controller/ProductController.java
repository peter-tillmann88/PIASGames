// src/main/java/com/eecs4413final/demo/controller/ProductController.java
package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.*;
import com.eecs4413final.demo.exception.ProductNotFoundException;
import com.eecs4413final.demo.model.Categories;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@CrossOrigin(
    origins = "http://localhost:5173", // The frontend URL
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE}, // Allowed HTTP methods
    allowedHeaders = "*", // Allow all headers (optional, can restrict if needed)
    maxAge = 3600 // Cache preflight response for 1 hour
)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            List<ProductResponseDTO> productDTOs = products.stream()
                    .map(product -> {
                        List<ImageDTO> imageDTOs = product.getImages() != null
                                ? product.getImages().stream()
                                .map(image -> new ImageDTO(
                                        image.getId(),
                                        image.getFileName(),
                                        image.getFileType(),
                                        image.getImageUrl()))
                                .collect(Collectors.toList())
                                : null;

                        Set<Categories> categories = product.getCategoryList();

                        return new ProductResponseDTO(
                                product.getProductId(),
                                product.getName(),
                                product.getDeveloper(),
                                product.getDescription(),
                                product.getPrice(),
                                product.getStock(),
                                product.getSaleMod(),
                                categories,
                                imageDTOs,
                                product.getPlatform()
                        );
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(productDTOs, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductResponseDTO> addProduct(
            @RequestPart("product") @Valid ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Product newProduct = productService.addProduct(productDTO, images);

            List<ImageDTO> imageDTOs = newProduct.getImages() != null
                    ? newProduct.getImages().stream()
                    .map(image -> new ImageDTO(
                            image.getId(),
                            image.getFileName(),
                            image.getFileType(),
                            image.getImageUrl()))
                    .collect(Collectors.toList())
                    : null;

            ProductResponseDTO responseDTO = new ProductResponseDTO(
                    newProduct.getProductId(),
                    newProduct.getName(),
                    newProduct.getDeveloper(),
                    newProduct.getDescription(),
                    newProduct.getPrice(),
                    newProduct.getStock(),
                    newProduct.getSaleMod(),
                    newProduct.getCategoryList(),
                    imageDTOs,
                    newProduct.getPlatform()
            );
            responseDTO.setMessage("Product successfully added.");
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            ProductResponseDTO errorResponse = new ProductResponseDTO();
            errorResponse.setMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO responseDTO = new ProductResponseDTO();
        try {
            Product product = productService.getById(id);

            List<ImageDTO> imageDTOs = product.getImages() != null
                    ? product.getImages().stream()
                    .map(image -> new ImageDTO(
                            image.getId(),
                            image.getFileName(),
                            image.getFileType(),
                            image.getImageUrl()))
                    .collect(Collectors.toList())
                    : null;

            responseDTO.setProductId(product.getProductId());
            responseDTO.setName(product.getName());
            responseDTO.setDeveloper(product.getDeveloper());
            responseDTO.setDescription(product.getDescription());
            responseDTO.setPrice(product.getPrice());
            responseDTO.setStock(product.getStock());
            responseDTO.setSaleMod(product.getSaleMod());
            responseDTO.setCategoryList(product.getCategoryList());
            responseDTO.setImages(imageDTOs);
            responseDTO.setPlatform(product.getPlatform());
            responseDTO.setMessage("Product found.");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            responseDTO.setMessage("Product not found for id: " + id);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<ProductResponseDTO> deleteProductById(@PathVariable Long id) {
        ProductResponseDTO responseDTO = new ProductResponseDTO();
        try {
            productService.deleteById(id);
            responseDTO.setMessage("Product successfully deleted.");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            responseDTO.setMessage("Product not found for id: " + id);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            responseDTO.setMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String query) {
        try {
            List<Product> products = productService.searchProductsByName(query);

            List<ProductResponseDTO> productDTOs = products.stream()
                    .map(product -> {
                        List<ImageDTO> imageDTOs = product.getImages() != null
                                ? product.getImages().stream()
                                .map(image -> new ImageDTO(
                                        image.getId(),
                                        image.getFileName(),
                                        image.getFileType(),
                                        image.getImageUrl()))
                                .collect(Collectors.toList())
                                : null;

                        Set<Categories> categories = product.getCategoryList();

                        return new ProductResponseDTO(
                                product.getProductId(),
                                product.getName(),
                                product.getDeveloper(),
                                product.getDescription(),
                                product.getPrice(),
                                product.getStock(),
                                product.getSaleMod(),
                                categories,
                                imageDTOs,
                                product.getPlatform()
                        );
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(productDTOs, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateDTO updateDTO) {
        ProductResponseDTO responseDTO = new ProductResponseDTO();
        try {
            Product updatedProduct = productService.updateProduct(id, updateDTO);

            List<ImageDTO> imageDTOs = updatedProduct.getImages() != null
                    ? updatedProduct.getImages().stream()
                    .map(image -> new ImageDTO(
                            image.getId(),
                            image.getFileName(),
                            image.getFileType(),
                            image.getImageUrl()))
                    .collect(Collectors.toList())
                    : null;

            responseDTO = new ProductResponseDTO(
                    updatedProduct.getProductId(),
                    updatedProduct.getName(),
                    updatedProduct.getDeveloper(),
                    updatedProduct.getDescription(),
                    updatedProduct.getPrice(),
                    updatedProduct.getStock(),
                    updatedProduct.getSaleMod(),
                    updatedProduct.getCategoryList(),
                    imageDTOs,
                    updatedProduct.getPlatform()
            );
            responseDTO.setMessage("Product updated successfully.");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            responseDTO.setMessage("Product not found for id: " + id);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
