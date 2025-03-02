package com.amalvadkar.lms.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;


@ConfigurationProperties(prefix = "lms")
@Validated
public record ApplicationProperties(

        @DefaultValue("false")
        Boolean emailSendEnabled,

        @NotEmpty(message = "appUrl property value is required")
        String appUrl,

        @NotNull(message = "otpLength property value is required")
        Integer otpLength,

        @NotEmpty(message = "jwtSecret property value is required")
        String jwtSecret,

        @DefaultValue("3600")
        Long jwtExpiryTimeInSec,

        @NotEmpty(message = "emailTemplatesLocation property value is required")
        String emailTemplatesLocation) {}
