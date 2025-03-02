package com.amalvadkar.lms.auth.app.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

public class OtpGenerator {

    public static String generateOtp(int length){
        return RandomStringUtils.randomNumeric(length);
    }
}
