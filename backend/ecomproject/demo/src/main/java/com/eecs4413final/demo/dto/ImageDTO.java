package com.eecs4413final.demo.dto;

public class ImageDTO {
    private Long id;
    private String fileName;

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getFileName(){
        return this.fileName;
    }

    public void setFileName(String name){
        this.fileName = name;
    }

}
