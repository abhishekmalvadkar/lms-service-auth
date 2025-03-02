package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends AuthException {

    public EmailAlreadyExistsException() {
        super("Email Already Exist", HttpStatus.CONFLICT.value());
    }

}
