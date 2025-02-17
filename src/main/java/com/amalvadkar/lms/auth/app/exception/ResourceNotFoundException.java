package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AuthException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
