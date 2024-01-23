package com.example.camelproject.exceptions;

public class EmailIsAlreadyUsedException extends RuntimeException{

    public EmailIsAlreadyUsedException(String message) {
        super(message);
    }

}
