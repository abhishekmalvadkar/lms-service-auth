package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class EmailNotFoundException extends AuthException {
    public EmailNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
