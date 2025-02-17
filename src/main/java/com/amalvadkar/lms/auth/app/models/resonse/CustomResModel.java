package com.amalvadkar.lms.auth.app.models.resonse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class CustomResModel {

    private Object data;

    private String message;

    private boolean success;

    private int code;

    public static CustomResModel success(Object data, String message){
        return builder()
                .data(data)
                .success(true)
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static CustomResModel fail( String message, HttpStatus httpStatus){
        return builder()
                .success(true)
                .code(httpStatus.value())
                .message(message)
                .build();
    }

}

