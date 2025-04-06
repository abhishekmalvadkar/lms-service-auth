package com.amalvadkar.lms.auth.app.controller.rest;

import com.amalvadkar.lms.auth.app.models.request.CreateAccountRequest;
import com.amalvadkar.lms.auth.app.models.request.SignInRequest;
import com.amalvadkar.lms.auth.app.models.request.VerifyAccountRequest;
import com.amalvadkar.lms.auth.app.models.request.VerifyOtpRequest;
import com.amalvadkar.lms.auth.app.models.request.VerifyTokenRequest;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import com.amalvadkar.lms.auth.app.models.resonse.VerifyTokenResponse;
import com.amalvadkar.lms.auth.app.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.amalvadkar.lms.auth.app.constants.AppConstants.REQUEST_HEADER_DEVICE;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final String ENDPOINT_CREATE_ACCOUNT = "/create-account";
    private static final String ENDPOINT_VERIFY_ACCOUNT = "/verify-account";
    private static final String ENDPOINT_SIGN_IN = "/sign-in";
    private static final String ENDPOINT_VERIFY_OTP = "/verify-otp";
    private static final String ENDPOINT_VERIFY_TOKEN = "/verify-token";

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
    public ResponseEntity<CustomResModel> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest,
                                                    @RequestHeader(REQUEST_HEADER_DEVICE) String device) {
        return this.authService.verifyOtp(verifyOtpRequest , device);
    }

    @PostMapping(ENDPOINT_VERIFY_TOKEN)
    public ResponseEntity<VerifyTokenResponse> verifyToken(@RequestBody VerifyTokenRequest verifyTokenRequest) {
        return ResponseEntity.ok(this.authService.verifyToken(verifyTokenRequest));
    }

}
