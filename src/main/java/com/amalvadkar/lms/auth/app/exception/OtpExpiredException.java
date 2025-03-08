package com.amalvadkar.lms.auth.app.exception;

import org.springframework.http.HttpStatus;

public class OtpExpiredException extends AuthException {

    public OtpExpiredException() {
        super("otp expired", HttpStatus.NOT_FOUND.value());
    }
}
