package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class InvalidOtpException extends AuthException {

    public InvalidOtpException() {
        super("invalid otp" , HttpStatus.BAD_REQUEST);
    }
}
