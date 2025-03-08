package com.amalvadkar.lms.auth.app.generator;

import com.amalvadkar.lms.auth.ApplicationProperties;
import com.amalvadkar.lms.auth.app.models.dto.OtpDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
@RequiredArgsConstructor
public class OtpGenerator {

    private final ApplicationProperties appProps;

    public OtpDto generate(){
        String otp = RandomStringUtils.randomNumeric(appProps.otpLength());
        Instant otpExpiryTime = prepareOtpExpiryTime();
        return new OtpDto(otp, otpExpiryTime);
    }

    private Instant prepareOtpExpiryTime() {
        return Instant.now().plus(appProps.otpExpiryDurationInMin(),
                MINUTES);
    }
}
