package com.amalvadkar.lms.auth.app.models.dto;

public record CreateTokenDto(String userId, String roleId, String device) {
}
