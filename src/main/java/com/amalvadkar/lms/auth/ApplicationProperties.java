package com.amalvadkar.lms.auth;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


@ConfigurationProperties(prefix = "lms")
@Validated
public record ApplicationProperties(

        @DefaultValue("false")
        boolean emailSendEnabled,

        @NotEmpty(message="App Url is required")
        String appUrl){

}
