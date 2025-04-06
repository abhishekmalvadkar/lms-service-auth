package com.amalvadkar.lms.auth.app.exception.handler;

import com.amalvadkar.lms.auth.app.exception.AuthException;
import com.amalvadkar.lms.auth.app.exception.TokenException;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import com.amalvadkar.lms.auth.app.models.resonse.VerifyTokenResponse;
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
    private static final String TOKEN_VERIFICATION_FAILED_MSG = "Token verification failed";

    @ExceptionHandler(TokenException.class)
    public VerifyTokenResponse handleTokenException(TokenException ex){
        logException(ex);
        return new VerifyTokenResponse(HttpStatus.UNAUTHORIZED.value(), TOKEN_VERIFICATION_FAILED_MSG);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<CustomResModel> handleAuthException(AuthException ex){
        logException(ex);
        return ResponseEntity.ok(CustomResModel.fail(List.of(ex.getMessage()), ex.getCode()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<CustomResModel> handleThrowable(Throwable ex){
        logException(ex);
        return ResponseEntity.ok(CustomResModel.fail(List.of(SOMETHING_WENT_WRONG_ERR_MSG) ,
                INTERNAL_SERVER_ERROR.value()));
    }

    private static void logException(Throwable ex) {
        log.error(EXCEPTION_OCCURRED_MSG, ex);
    }

}
