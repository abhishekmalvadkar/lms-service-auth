package com.amalvadkar.lms.auth.app.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record VerifyAccountRequest(
        @Email(message = "Email is not valid")
        @NotEmpty(message = "Email is required")
        String email,

        @NotEmpty(message = "VerificationToken is required")
        String verificationToke
) {


}