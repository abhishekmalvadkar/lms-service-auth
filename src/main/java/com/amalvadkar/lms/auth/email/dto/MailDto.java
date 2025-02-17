package com.amalvadkar.lms.auth.email.dto;

import java.util.Map;

public record MailDto(
        String subject,

        String toMail,

        Map<String, Object> props,

        String templateFileName
) {
}
