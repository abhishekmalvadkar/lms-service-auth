package com.amalvadkar.lms.auth.app.models.resonse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResModel {

    private Object data;

    private String message;

    private boolean success;

    private int code;

    private List<String> errors;

    public static CustomResModel success(Object data, String message){
        return builder()
                .data(data)
                .success(true)
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static CustomResModel fail(List<String> errors, int code){
        return builder()
                .success(false)
                .code(code)
                .errors(errors)
                .build();
    }

}

