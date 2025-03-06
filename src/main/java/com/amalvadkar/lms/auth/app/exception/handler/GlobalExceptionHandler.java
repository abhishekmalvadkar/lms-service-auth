package com.amalvadkar.lms.auth.app.exception.handler;

import com.amalvadkar.lms.auth.app.exception.ResourceAlreadyExistsException;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String EXCEPTION_OCCURRED_MSG = "Exception occurred : ";

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<CustomResModel> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex){
        logException(ex);
        return ResponseEntity.status(HttpStatus.valueOf(ex.getCode()))
                .body(CustomResModel.fail(List.of(ex.getMessage()), ex.getCode()));
    }

    private static void logException(Throwable ex) {
        log.error(EXCEPTION_OCCURRED_MSG, ex);
    }

}
