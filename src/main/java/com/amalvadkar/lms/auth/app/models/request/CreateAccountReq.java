package com.amalvadkar.lms.auth.app.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAccountReq(

        @NotEmpty(message = "First name is required")
        String firstName,

        @NotEmpty(message = "Last name is required")
        String lastName,

        @Email(message = "Invalid email")
        @NotNull(message = "Email is required")
        String email

) {
}
