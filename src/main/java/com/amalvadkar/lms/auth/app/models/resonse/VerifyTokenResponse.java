package com.amalvadkar.lms.auth.app.models.resonse;

public record VerifyTokenResponse(String userId, String roleId, String device, boolean isValid) {
    public VerifyTokenResponse(boolean isValid) {
        this(null, null, null, isValid);
    }
}
