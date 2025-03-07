package com.amalvadkar.lms.auth.app.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequest {

    private String email;
    private String otp;
}