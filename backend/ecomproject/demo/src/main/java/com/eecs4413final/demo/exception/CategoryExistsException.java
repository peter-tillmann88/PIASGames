package com.eecs4413final.demo.exception;

public class CategoryExistsException extends RuntimeException{
    public CategoryExistsException(String message) {
        super(message);
    }
}
