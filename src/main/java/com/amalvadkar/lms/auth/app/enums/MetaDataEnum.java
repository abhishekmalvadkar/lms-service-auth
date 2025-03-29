package com.amalvadkar.lms.auth.app.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MetaDataEnum {

    TAG_DROP_DOWN_OPTIONS("tagDropdownOptions");

    private final String value;

    public String value() {
        return value;
    }
}
