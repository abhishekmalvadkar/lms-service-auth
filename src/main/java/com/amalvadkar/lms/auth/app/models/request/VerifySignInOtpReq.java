package com.amalvadkar.lms.auth.app.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifySignInOtpReq {

    private String email;
    private String otp;
}