package com.amalvadkar.lms.auth.app.controller.rest;

import com.amalvadkar.lms.auth.app.models.request.CreateAccountReq;
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

    private final AuthService authService;


    @PostMapping("/create-account")
    public ResponseEntity<CustomResModel> CreateAccount(@Valid @RequestBody CreateAccountReq createAccountReq) {
        return ResponseEntity.ok(authService.createAcoount(createAccountReq));
    }


}
