package com.amalvadkar.lms.auth.app.models.resonse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtTokenVerifyRes {

    private String userId;
    private Object roleId;




}
