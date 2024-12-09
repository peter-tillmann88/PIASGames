package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.ImageDTO;
import com.eecs4413final.demo.exception.ImageNotFoundException;
import com.eecs4413final.demo.exception.ImageTooLargeException;
import com.eecs4413final.demo.model.Image;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.repository.ImageRepository;
import com.eecs4413final.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService{

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final SupabaseStorageService storageService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, ProductRepository productRepository, SupabaseStorageService storageService) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.storageService = storageService;
    }

    @Override
    public Image getById(Long id){
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image does not exist with Id: " + id));
    }

    @Override
    public void deleteById(Long id){
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image does not exist with Id: " + id));
        // Delete the image from Supabase Storage
        String fileName = image.getFileName();
        Mono<Void> deleteMono = storageService.deleteImage(fileName);
        deleteMono.block(); // Blocking for simplicity; consider reactive chains for production
        // Delete from the database
        imageRepository.deleteById(id);
    }

    private void validateImage(MultipartFile file) throws Exception {
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equalsIgnoreCase("image/jpeg") &&
                        !contentType.equalsIgnoreCase("image/png") &&
                        !contentType.equalsIgnoreCase("image/gif"))) {
            throw new Exception("Unsupported file type: " + contentType);
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
            throw new ImageTooLargeException("File size exceeds the maximum limit of 5MB");
        }
    }

    @Override
    public ImageDTO addImage(Long productId, MultipartFile file) throws Exception {
        System.out.println("Adding image for product ID: " + productId);
        validateImage(file);
        // Find the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ImageNotFoundException("Product not found with Id: " + productId));

        // Generate a unique filename
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        System.out.println("Generated file name: " + fileName);

        // Upload image to Supabase Storage and get the URL
        Mono<String> imageUrlMono = storageService.uploadImage(file, fileName);
        String imageUrl = imageUrlMono.block(); // Blocking for simplicity
        System.out.println("Uploaded image URL: " + imageUrl);

        // Create and save the Image entity
        Image image = new Image();
        image.setFileName(fileName);
        image.setFileType(file.getContentType());
        image.setImageUrl(imageUrl);
        image.setProduct(product);

        Image savedImage = imageRepository.save(image);
        System.out.println("Saved image with ID: " + savedImage.getId());

        // Convert to DTO
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(savedImage.getId());
        imageDTO.setFileName(savedImage.getFileName());
        imageDTO.setFileType(savedImage.getFileType());
        imageDTO.setImageUrl(savedImage.getImageUrl());

        return imageDTO;
    }

    @Override
    public List<ImageDTO> addImages(Long productId, List<MultipartFile> files){
        System.out.println("Adding multiple images for product ID: " + productId);
        return files.stream().map(file -> {
            try {
                return addImage(productId, file);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image: " + file.getOriginalFilename(), e);
            }
        }).collect(Collectors.toList());
    }
}