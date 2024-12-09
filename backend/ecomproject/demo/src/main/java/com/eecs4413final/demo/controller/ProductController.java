package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.ProductDTO;
import com.eecs4413final.demo.dto.ProductResponseDTO;
import com.eecs4413final.demo.exception.ProductNotFoundException;
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

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductResponseDTO> addProduct(
            @RequestPart("product") @Valid ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Product newProduct = productService.addProduct(productDTO, images);
            ProductResponseDTO responseDTO = new ProductResponseDTO(
                    newProduct.getProductId(),
                    newProduct.getName(),
                    newProduct.getDeveloper(),
                    newProduct.getDescription(),
                    newProduct.getPrice(),
                    newProduct.getStock(),
                    newProduct.getSaleMod(),
                    newProduct.getCategoryList(),
                    newProduct.getImages(),
                    newProduct.getPlatform()
            );
            responseDTO.setMessage("Product successfully added.");
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Log exception details
            ProductResponseDTO errorResponse = new ProductResponseDTO();
            errorResponse.setMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO responseDTO = new ProductResponseDTO();
        try {
            Product product = productService.getById(id);
            responseDTO.setProductId(product.getProductId());
            responseDTO.setName(product.getName());
            responseDTO.setDeveloper(product.getDeveloper());
            responseDTO.setDescription(product.getDescription());
            responseDTO.setPrice(product.getPrice());
            responseDTO.setStock(product.getStock());
            responseDTO.setMod(product.getSaleMod());
            responseDTO.setCategoryList(product.getCategoryList());
            responseDTO.setImages(product.getImages());
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

    @DeleteMapping("del/{id}")
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
}