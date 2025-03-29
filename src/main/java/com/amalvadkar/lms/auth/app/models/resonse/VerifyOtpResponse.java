package com.amalvadkar.lms.auth.app.models.resonse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

import static java.util.Objects.isNull;

@Getter
@Setter
public class VerifyOtpResponse{

    @Setter(AccessLevel.NONE)
    private boolean firstLogin;

    @Setter(AccessLevel.NONE)
    private Instant lastLoginTime;

    private Map<String,Object> metaData;


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
