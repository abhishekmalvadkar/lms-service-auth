package com.amalvadkar.lms.auth.app.enums;

public enum ErrorMsgEnum {

    EMAIL_NOT_EXIST("Email not exist");

    String value;

    private ErrorMsgEnum(String value){
        this.value=value;
    }

    public String getValue(){
        return value;
    }
}
