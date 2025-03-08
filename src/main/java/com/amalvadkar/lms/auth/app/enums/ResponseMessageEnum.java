package com.amalvadkar.lms.auth.app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessageEnum {

    CREATED_SUCCESSFULLY("Created Successfully");

    private final String value;
}
