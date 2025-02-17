package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistException extends AuthException {
    public EmailAlreadyExistException(String message)
    {
        super(message,HttpStatus.CONFLICT.value());
    }
}
