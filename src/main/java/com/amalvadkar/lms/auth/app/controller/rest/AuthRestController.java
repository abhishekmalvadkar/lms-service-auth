package com.amalvadkar.lms.auth.app.controller.rest;

import com.amalvadkar.lms.auth.app.models.request.CreateAccountReq;
import com.amalvadkar.lms.auth.app.models.request.VerifyAccountRequest;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import com.amalvadkar.lms.auth.app.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lms/auth")
@RequiredArgsConstructor
public class AuthRestController {

    public static final String ENDPOINT_VERIFY_ACCOUNT = "/verify-account";
    public static final String ENDPOINT_CREATE_ACCOUNT = "/create-account";

    private final AuthService authService;


    @PostMapping(ENDPOINT_CREATE_ACCOUNT)
    public ResponseEntity<CustomResModel> CreateAccount(@Valid @RequestBody CreateAccountReq createAccountReq) {
        return ResponseEntity.ok(authService.createAcoount(createAccountReq));
    }

    @PostMapping(ENDPOINT_VERIFY_ACCOUNT)
    public ResponseEntity<CustomResModel> verifyAccount(@RequestBody VerifyAccountRequest verifyAccountRequest){
      return ResponseEntity.ok(authService.verifyAccount(verifyAccountRequest));

    }


}
