package com.eecs4413final.demo.dto;

public class ObjectResponseDTO {
    private String response;
    private Object object;

   
    public ObjectResponseDTO() {}



    public ObjectResponseDTO(String response, Object object) {
        this.response = response;
        this.object = object;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


    public Object getObject() {
        return object;
    }


    public void setObject(Object object) {
        this.object = object;
    }
}
