package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class OtpExpireException extends AuthException {
    public OtpExpireException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
