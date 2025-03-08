package com.amalvadkar.lms.auth.app.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignInRequest(

        @Email(message = "email is invalid")
        @NotNull(message = "email is required")
        String email) {
}
