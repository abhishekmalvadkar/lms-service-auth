package com.amalvadkar.lms.auth.app.controller.rest.auth;

import com.amalvadkar.lms.auth.app.exception.InvalidOtpException;
import com.amalvadkar.lms.auth.app.exception.OtpExpiredException;
import com.amalvadkar.lms.auth.app.models.request.VerifyOtpRequest;
import com.amalvadkar.lms.auth.app.services.AuthService;
import com.amalvadkar.lms.auth.common.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthVerifyOtpWebTest extends AbstractWebTest {

    @MockitoBean
    AuthService authService;

    private static final String VERIFY_OTP_URL = "/api/auth/verify-otp";

    @Test
    void should_send_error_invalid_otp_if_invalid_otp_passed() throws Exception {

        // Given: Mock service response
        when(authService.verifyOtp(any(VerifyOtpRequest.class), any(String.class)))
                .thenThrow(new InvalidOtpException());

        String requestPayload = """
                    {
                        "email": "john.doe@example.com",
                        "otp": "123456"
                    }
                """;

        // When: Sending request
        mockMvc.perform(post(VERIFY_OTP_URL)
                        .header("device", "web")
                        .contentType(APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[0]").value("invalid otp"))
                .andExpect(jsonPath("$.data").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.message").doesNotHaveJsonPath());

        // Then: Verify service is called once
        verify(authService).verifyOtp(any(VerifyOtpRequest.class), any(String.class));
    }

    @Test
    void should_send_error_otp_expired_if_expired_otp_passed() throws Exception {

        // Given: Mock service response
        when(authService.verifyOtp(any(VerifyOtpRequest.class), any(String.class)))
                .thenThrow(new OtpExpiredException());

        String requestPayload = """
                    {
                        "email": "john.doe@example.com",
                        "otp": "123456"
                    }
                """;

        // When: Sending request
        mockMvc.perform(post(VERIFY_OTP_URL)
                        .header("device", "web")
                        .contentType(APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[0]").value("otp expired"))
                .andExpect(jsonPath("$.data").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.message").doesNotHaveJsonPath());

        // Then: Verify service is called once
        verify(authService).verifyOtp(any(VerifyOtpRequest.class), any(String.class));
    }
}