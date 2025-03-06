package com.amalvadkar.lms.auth.app.exception.handler;

import com.amalvadkar.lms.auth.app.exception.ResourceAlreadyExistsException;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<CustomResModel> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.valueOf(ex.getCode()))
                .body(CustomResModel.fail(List.of(ex.getMessage()), ex.getCode()));
    }

}
