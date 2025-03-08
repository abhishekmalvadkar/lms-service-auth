package com.amalvadkar.lms.auth.app.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class AccountLockedException extends AuthException {
    public AccountLockedException() {
        super("Account is locked", BAD_REQUEST);
    }
}
