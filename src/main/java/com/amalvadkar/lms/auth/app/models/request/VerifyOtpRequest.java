package com.amalvadkar.lms.auth.app.models.request;

public record VerifyOtpRequest(String email, String otp) {
}