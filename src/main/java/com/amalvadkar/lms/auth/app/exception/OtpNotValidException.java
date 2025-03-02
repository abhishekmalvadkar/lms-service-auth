package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class OtpNotValidException extends AuthException {
    public OtpNotValidException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
