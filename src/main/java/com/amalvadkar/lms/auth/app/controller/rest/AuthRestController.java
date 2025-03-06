package com.amalvadkar.lms.auth.app.controller.rest;

import com.amalvadkar.lms.auth.app.models.request.*;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import com.amalvadkar.lms.auth.app.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    public static final String ENDPOINT_CREATE_ACCOUNT = "/create-account";
    public static final String ENDPOINT_VERIFY_ACCOUNT = "/verify-account";
    public static final String ENDPOINT_SIGN_IN = "/sign-in";
    public static final String ENDPOINT_VERIFY_OTP = "/verify-otp";
    public static final String ENDPOINT_VERIFY_JWT_TOKEN = "/verify-jwt-token";

    private final AuthService authService;


    @PostMapping(ENDPOINT_CREATE_ACCOUNT)
    public ResponseEntity<CustomResModel> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.createAccount(createAccountRequest));
    }

    @PostMapping(ENDPOINT_VERIFY_ACCOUNT)
    public ResponseEntity<CustomResModel> verifyAccount(@Valid @RequestBody VerifyAccountRequest verifyAccountRequest) {
        return ResponseEntity.ok(authService.verifyAccount(verifyAccountRequest));

    }

    @PostMapping(ENDPOINT_SIGN_IN)
    public ResponseEntity<CustomResModel> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(this.authService.signIn(signInRequest));
    }

    @PostMapping(ENDPOINT_VERIFY_OTP)
    public ResponseEntity<CustomResModel> verifySignInOtp(@RequestBody VerifySignInOtpReq verifySignInOtpReq) {
        return ResponseEntity.ok(this.authService.verifySignInOtp(verifySignInOtpReq));

    }

    @PostMapping(ENDPOINT_VERIFY_JWT_TOKEN)
    public ResponseEntity<CustomResModel> verifyJwtToken(@RequestBody VerifyJwtTokenReq verifyJwtTokenReq) {
        return ResponseEntity.ok(this.authService.verifyJwtToken(verifyJwtTokenReq.authToken()));
    }

}
