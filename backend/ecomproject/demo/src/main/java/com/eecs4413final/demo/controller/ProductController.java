package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.ImageDTO;
import com.eecs4413final.demo.dto.ProductDTO;
import com.eecs4413final.demo.dto.ProductResponseDTO;
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
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
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

            // Map each Product entity to ProductResponseDTO
            List<ProductResponseDTO> productDTOs = products.stream()
                    .map(product -> {
                        // Map Images to ImageDTOs
                        List<ImageDTO> imageDTOs = product.getImages() != null
                                ? product.getImages().stream()
                                .map(image -> new ImageDTO(
                                        image.getId(),
                                        image.getFileName(),
                                        image.getFileType(),
                                        image.getImageUrl()))
                                .collect(Collectors.toList())
                                : null;

                        // Map Categories to CategoryDTOs (if you have CategoryDTO)
                        // Otherwise, ensure Categories have @JsonIgnore on back references
                        Set<Categories> categories = product.getCategoryList();

                        // Create ProductResponseDTO
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
            e.printStackTrace(); // Log the exception for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductResponseDTO> addProduct(
            @RequestPart("product") @Valid ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try{
            if (images != null && !images.isEmpty()) {
                System.out.println("the images are found in controller");
            }
            else{
                System.out.println("the images are not in controller");
            }
        }
        catch (Exception e){
            e.printStackTrace(); // Log exception details
            ProductResponseDTO errorResponse = new ProductResponseDTO();
            errorResponse.setMessage("An unexpected error occurred when getting the image in product controller: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Product newProduct = productService.addProduct(productDTO, images);

            // Map Image entities to ImageDTOs
            List<ImageDTO> imageDTOs = newProduct.getImages() != null
                    ? newProduct.getImages().stream()
                    .map(image -> {
                        ImageDTO dto = new ImageDTO();
                        dto.setId(image.getId());
                        dto.setFileName(image.getFileName());
                        dto.setFileType(image.getFileType());
                        dto.setImageUrl(image.getImageUrl());
                        return dto;
                    })
                    .collect(Collectors.toList())
                    : null;

            // Create ProductResponseDTO with ImageDTOs
            ProductResponseDTO responseDTO = new ProductResponseDTO(
                    newProduct.getProductId(),
                    newProduct.getName(),
                    newProduct.getDeveloper(),
                    newProduct.getDescription(),
                    newProduct.getPrice(),
                    newProduct.getStock(),
                    newProduct.getSaleMod(),
                    newProduct.getCategoryList(),
                    imageDTOs, // Use mapped ImageDTOs
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

            // Map Image entities to ImageDTOs
            List<ImageDTO> imageDTOs = product.getImages() != null
                    ? product.getImages().stream()
                    .map(image -> {
                        ImageDTO dto = new ImageDTO();
                        dto.setId(image.getId());
                        dto.setFileName(image.getFileName());
                        dto.setFileType(image.getFileType());
                        dto.setImageUrl(image.getImageUrl());
                        return dto;
                    })
                    .collect(Collectors.toList())
                    : null;

            // Set fields in ProductResponseDTO
            responseDTO.setProductId(product.getProductId());
            responseDTO.setName(product.getName());
            responseDTO.setDeveloper(product.getDeveloper());
            responseDTO.setDescription(product.getDescription());
            responseDTO.setPrice(product.getPrice());
            responseDTO.setStock(product.getStock());
            responseDTO.setMod(product.getSaleMod());
            responseDTO.setCategoryList(product.getCategoryList());
            responseDTO.setImages(imageDTOs); // Use mapped ImageDTOs
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
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String query) {
        try {
            List<Product> products = productService.searchProductsByName(query);

            // Map each Product entity to ProductResponseDTO
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
            e.printStackTrace(); // Log the exception for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}