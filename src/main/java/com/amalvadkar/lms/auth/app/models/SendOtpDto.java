package com.amalvadkar.lms.auth.app.models;

import java.time.Instant;
import java.time.LocalDateTime;

public record SendOtpDto(
        String otp,
        Instant otpExpireTime
) {
}
