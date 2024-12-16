package com.eecs4413final.demo.dto;

public class ImageDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private String imageUrl;


    public ImageDTO() {
    }

    public ImageDTO(Long id, String fileName, String fileType, String imageUrl) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.imageUrl = imageUrl;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFileType(){
        return fileType;
    }

    public void setFileType(String fileType){
        this.fileType = fileType;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
}