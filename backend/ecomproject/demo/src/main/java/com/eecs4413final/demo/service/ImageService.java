package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.ImageDTO;
import com.eecs4413final.demo.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getById(Long id);
    void deleteById(Long id);
    ImageDTO addImage(Long productId, MultipartFile file) throws Exception;
    List<ImageDTO> addImages(Long productId, List<MultipartFile> files) throws Exception;
}