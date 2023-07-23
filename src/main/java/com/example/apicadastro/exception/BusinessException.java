package com.example.apicadastro.exception;


public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
