package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class TokenException extends AuthException {
    public TokenException(String message) {
        super(message , HttpStatus.BAD_REQUEST);
    }
}
