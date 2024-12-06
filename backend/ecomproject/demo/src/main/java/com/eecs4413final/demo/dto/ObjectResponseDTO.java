package com.eecs4413final.demo.dto;

public class ObjectResponseDTO {
    private String response;
    private Object object;

    // Default constructor
    public ObjectResponseDTO() {}



    // Parameterized constructor
    public ObjectResponseDTO(String response, Object object) {
        this.response = response;
        this.object = object;
    }

    // Getter for response
    public String getResponse() {
        return response;
    }

    // Setter for response
    public void setResponse(String response) {
        this.response = response;
    }

    // Getter for object
    public Object getObject() {
        return object;
    }

    // Setter for object
    public void setObject(Object object) {
        this.object = object;
    }
}
