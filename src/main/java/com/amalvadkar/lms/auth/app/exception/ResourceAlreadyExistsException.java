package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends AuthException {

    public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}
