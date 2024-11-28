package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.ImageDTO;
import com.eecs4413final.demo.exception.ImageNotFoundException;
import com.eecs4413final.demo.model.Image;
import com.eecs4413final.demo.repository.ImageRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageServiceImpl implements  ImageService{

    private final ImageRepository imageRepository;


    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getById(Long id){
        Image image = imageRepository.findById(id).orElse(null);
        if(image != null){
            return image;
        }else{
            throw new ImageNotFoundException("Image does not exist with Id: "+ id);
        }
    }


    public void deleteById(Long id){
        Image image = imageRepository.findById(id).orElse(null);
        if(image != null){
            imageRepository.deleteById(id);
        }else{
            throw new ImageNotFoundException("Image does not exist with Id: "+ id);
        }
    }

    public ImageDTO addImage(ImageDTO image){
        return null;
    }

    public List<ImageDTO> addImages(Long productId, List<MultipartFile> files){
//        Product product =
        return null;
    }
}
