package com.example.camelproject.exceptions;

public class InvalidBodyException extends RuntimeException{

    public InvalidBodyException(String message) {
        super(message);
    }

}
