package com.amalvadkar.lms.auth.app.models.request;

import jakarta.validation.constraints.Email;

public record SignInRequest(

        @Email(message="email is invalid")
        String email
) {
}
