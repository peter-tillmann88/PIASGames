package com.eecs4413final.demo.exception;

public class ImageTooLargeException extends RuntimeException {
    public ImageTooLargeException(String message) {
        super(message);
    }
}