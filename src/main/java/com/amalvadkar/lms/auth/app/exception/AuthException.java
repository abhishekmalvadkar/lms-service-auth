package com.amalvadkar.lms.auth.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {

    private final Integer code;

    public AuthException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
