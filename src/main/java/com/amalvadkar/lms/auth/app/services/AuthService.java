package com.amalvadkar.lms.auth.app.services;

import com.amalvadkar.lms.auth.ApplicationProperties;
import com.amalvadkar.lms.auth.app.entities.UserEntity;
import com.amalvadkar.lms.auth.app.enums.ResponseMsgEnum;
import com.amalvadkar.lms.auth.app.models.request.CreateAccountReq;
import com.amalvadkar.lms.auth.app.models.request.VerifyAccountRequest;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import com.amalvadkar.lms.auth.app.repositories.RoleRepo;
import com.amalvadkar.lms.auth.app.repositories.UserRepo;
import com.amalvadkar.lms.auth.email.dto.MailDto;
import com.amalvadkar.lms.auth.email.sender.EmailSender;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    public static final String VERIFY_ACCOUNT_EMAIL_SUBJECT = "Verify Your Lms Account";
    public static final String VERIFY_ACCOUNT_EMAIL_TEMPLATE_FILE_NAME = "verify-account";
    public static final String VERIFY_ACCOUNT_SUCCESS_HTML_CONTENT = """
            <h2>Customer Verification</h2>
            <p class="text-success fw-bold">Congratulations! Your account has been verified.</p>
            """;
    public static final String VERIFY_ACCOUNT_FAIL_HTML_CONTENT = """
             <h2>Customer Verification</h2>
             <p class="text-danger fw-bold">Your account was already verified, or the verification code is invalid.</p>
            """;

    public static final String VERIFIED_SUCCESSFULLY_RES_MSG = "Verified successfully";
    public static final String VERIFICATION_FAILED_RES_MSG = "Verification failed";


    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final EmailSender emailSender;
    private final ApplicationProperties appProps;

    @Transactional
    public CustomResModel createAcoount(@Valid CreateAccountReq createAccountReq) {

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(createAccountReq.firstName());
        userEntity.setLastName(createAccountReq.lastName());
        userEntity.setVerificationToken(UUID.randomUUID().toString());
        userEntity.setEmail(createAccountReq.email());
        userEntity.setRole(roleRepo.fetchRoleBasedOnCode());
        UserEntity createdUser = userRepo.save(userEntity);

        sendVerificationEmail(new VerifyEmailDTO(userEntity.getEmail(), userEntity.getVerificationToken(), userEntity.fullName()));
        return CustomResModel.success(Map.of("userId", createdUser.getId()), ResponseMsgEnum.CREATED_SUCCESSFULLY_MSG.getValue());
    }

    @Transactional
    public CustomResModel verifyAccount(VerifyAccountRequest verifyEmailReq) {
        Optional<UserEntity> userEntityOpt = this.userRepo.findByEmailAndToken(verifyEmailReq.email(),
                verifyEmailReq.verificationToke());
        return userEntityOpt.map(this::activateAccount)
                .orElseGet(() ->
                        CustomResModel.success(VERIFY_ACCOUNT_FAIL_HTML_CONTENT, VERIFICATION_FAILED_RES_MSG));
    }

    private CustomResModel activateAccount(UserEntity userEntity) {
        userEntity.setVerificationToken(null);
        userEntity.setActive(true);
        userRepo.save(userEntity);
        return CustomResModel.success(VERIFY_ACCOUNT_SUCCESS_HTML_CONTENT, VERIFIED_SUCCESSFULLY_RES_MSG);
    }

    private void sendVerificationEmail(VerifyEmailDTO verifyEmailDTO) {
        String params =
                "email=" + encode(verifyEmailDTO.email(), UTF_8) + "&token=" + encode(verifyEmailDTO.token(), UTF_8);
        String verificationUrl = appProps.appUrl() + "/verify-account?" + params;

        MailDto mailDto = new MailDto(
                VERIFY_ACCOUNT_EMAIL_SUBJECT,
                verifyEmailDTO.email(),
                Map.of("username", verifyEmailDTO.username(), "verificationUrl", verificationUrl),
                VERIFY_ACCOUNT_EMAIL_TEMPLATE_FILE_NAME
        );
        emailSender.sendInAsync(mailDto);
    }


    record VerifyEmailDTO(String email, String token, String username) {
    }


}
