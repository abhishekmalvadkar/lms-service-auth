package com.amalvadkar.lms.auth.app.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(

        @NotEmpty(message = "firstName is required")
        String firstName,

        @NotEmpty(message = "lastName is required")
        String lastName,

        @Email(message = "Invalid email")
        @NotNull(message = "email is required")
        String email) {
}
