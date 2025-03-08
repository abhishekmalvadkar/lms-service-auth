package com.amalvadkar.lms.auth.app.models.dto;

import java.time.Instant;

public record OtpDto(String otp, Instant otpExpiryTime) {
}
