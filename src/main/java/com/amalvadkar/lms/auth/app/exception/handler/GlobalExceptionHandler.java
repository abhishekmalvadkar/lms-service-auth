package com.amalvadkar.lms.auth.app.exception.handler;

import com.amalvadkar.lms.auth.app.exception.AuthException;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String EXCEPTION_OCCURRED_MSG = "Exception occurred : ";
    private static final String SOMETHING_WENT_WRONG_ERR_MSG = "Something went wrong, please try later";

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<CustomResModel> handleAuthException(AuthException ex){
        logException(ex);
        return ResponseEntity.status(HttpStatus.valueOf(ex.getCode()))
                .body(CustomResModel.fail(List.of(ex.getMessage()), ex.getCode()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<CustomResModel> handleThrowable(Throwable ex){
        logException(ex);
        return ResponseEntity.internalServerError()
                .body(CustomResModel.fail(List.of(SOMETHING_WENT_WRONG_ERR_MSG) ,
                        INTERNAL_SERVER_ERROR.value()));
    }

    private static void logException(Throwable ex) {
        log.error(EXCEPTION_OCCURRED_MSG, ex);
    }

}
