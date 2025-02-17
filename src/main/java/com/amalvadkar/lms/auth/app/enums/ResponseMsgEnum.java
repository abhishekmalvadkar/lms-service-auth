package com.amalvadkar.lms.auth.app.enums;

public enum ResponseMsgEnum {

    CREATED_SUCCESSFULLY_MSG("Created Successfully"),
    DELETED_SUCCESSFULLY_MSG("Deleted successfully"),
    FETCHED_SUCCESSFULLY_MSG("Fetched successfully"),
    UPDATED_SUCCESSFULLY_MSG("Updated successfully");

    private String value;

    private ResponseMsgEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
