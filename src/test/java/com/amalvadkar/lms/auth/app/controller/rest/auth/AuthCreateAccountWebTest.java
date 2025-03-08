package com.amalvadkar.lms.auth.app.controller.rest.auth;

import com.amalvadkar.lms.auth.app.exception.ResourceAlreadyExistsException;
import com.amalvadkar.lms.auth.app.models.request.CreateAccountRequest;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import com.amalvadkar.lms.auth.app.services.AuthService;
import com.amalvadkar.lms.auth.common.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthCreateAccountWebTest extends AbstractWebTest {

    @MockitoBean
    AuthService authService;

    private static final String CREATE_ACCOUNT_URL = "/api/auth/create-account";

    @Test
    void should_create_account_successfully() throws Exception {

        // Given: Mock service response
        CustomResModel responseModel = CustomResModel.success(Map.of("userId", 123L), "Created Successfully");
        when(authService.createAccount(any(CreateAccountRequest.class))).thenReturn(responseModel);


        String requestPayload = """
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com"
                    }
                """;

        // When: Sending request
        mockMvc.perform(post(CREATE_ACCOUNT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.userId").value(123L))
                .andExpect(jsonPath("$.message").value("Created Successfully"));

        // Then: Verify service is called once
        verify(authService).createAccount(any(CreateAccountRequest.class));
    }

    @Test
    void should_send_error_like_email_already_exists_if_existing_email_passed_while_create_account() throws Exception {

        // Given: Mock service response
        when(authService.createAccount(any(CreateAccountRequest.class)))
                .thenThrow(new ResourceAlreadyExistsException("Email Already Exist"));

        String requestPayload = """
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com"
                    }
                """;

        // When: Sending request
        mockMvc.perform(post(CREATE_ACCOUNT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[0]").value("Email Already Exist"))
                .andExpect(jsonPath("$.data").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.message").doesNotHaveJsonPath());

        // Then: Verify service is called once
        verify(authService).createAccount(any(CreateAccountRequest.class));
    }

}