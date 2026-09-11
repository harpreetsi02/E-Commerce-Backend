package com.backend.ecommercebackend.exception;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(String message){
        super(message);
    }
}
