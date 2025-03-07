package com.amalvadkar.lms.auth.app.models.resonse;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import static java.util.Objects.isNull;

@Getter
@Setter
public class VerifyOtpResponse {

    private boolean firstLogin;
    private Instant lastLoginTime;

    public void setLastLoginDetails(Instant lastLoginTime) {
        if (isNull(lastLoginTime)){
            this.lastLoginTime = Instant.now();
            this.firstLogin = true;
        } else {
            this.lastLoginTime = lastLoginTime;
            this.firstLogin = false;
        }
    }
}
