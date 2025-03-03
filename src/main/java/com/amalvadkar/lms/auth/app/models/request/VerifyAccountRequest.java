package com.amalvadkar.lms.auth.app.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VerifyAccountRequest(

        @Email(message = "email is not valid")
        @NotNull(message = "email is required")
        String email,

        @NotEmpty(message = "verificationToken is required")
        String verificationToken) {
}