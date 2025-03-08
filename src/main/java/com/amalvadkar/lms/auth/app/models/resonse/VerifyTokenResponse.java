package com.amalvadkar.lms.auth.app.models.resonse;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VerifyTokenResponse(String userId, String roleId, String device, boolean isValid) {
    public VerifyTokenResponse(boolean isValid) {
        this(null, null, null, isValid);
    }
}
