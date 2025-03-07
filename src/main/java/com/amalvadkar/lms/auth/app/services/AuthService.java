package com.amalvadkar.lms.auth.app.services;

import com.amalvadkar.lms.auth.ApplicationProperties;
import com.amalvadkar.lms.auth.app.constants.AppConstants;
import com.amalvadkar.lms.auth.app.entities.UserEntity;
import com.amalvadkar.lms.auth.app.enums.ErrorMsgEnum;
import com.amalvadkar.lms.auth.app.enums.UserStatusEnum;
import com.amalvadkar.lms.auth.app.exception.EmailNotFoundException;
import com.amalvadkar.lms.auth.app.exception.InvalidOtpException;
import com.amalvadkar.lms.auth.app.exception.OtpExpiredException;
import com.amalvadkar.lms.auth.app.generator.OtpGenerator;
import com.amalvadkar.lms.auth.app.helper.TokenHelper;
import com.amalvadkar.lms.auth.app.models.SendOtpDto;
import com.amalvadkar.lms.auth.app.models.request.CreateAccountRequest;
import com.amalvadkar.lms.auth.app.models.request.SignInRequest;
import com.amalvadkar.lms.auth.app.models.request.VerifyAccountRequest;
import com.amalvadkar.lms.auth.app.models.request.VerifyOtpRequest;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import com.amalvadkar.lms.auth.app.models.resonse.VerifyOtpResponse;
import com.amalvadkar.lms.auth.app.repositories.RoleRepo;
import com.amalvadkar.lms.auth.app.repositories.UserRepo;
import com.amalvadkar.lms.auth.email.dto.MailDto;
import com.amalvadkar.lms.auth.email.sender.EmailSender;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import static com.amalvadkar.lms.auth.app.constants.AppConstants.*;
import static com.amalvadkar.lms.auth.app.enums.ResponseMsgEnum.CREATED_SUCCESSFULLY_MSG;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final EmailSender emailSender;
    private final ApplicationProperties appProps;
    private final TokenHelper tokenHelper;

    @Transactional
    public CustomResModel createAccount(CreateAccountRequest createAccountRequest) {
        this.userRepo.throwIfEmailExists(createAccountRequest.email());
        UserEntity savedUserEntity = saveNewUserToDb(createAccountRequest);
        sendVerifyAccountEmail(prepareVerifyEmailDto(savedUserEntity));
        return prepareCreateAccountResponse(savedUserEntity);
    }

    private static CustomResModel prepareCreateAccountResponse(UserEntity savedUserEntity) {
        return CustomResModel.success(
                Map.of(AppConstants.USER_ID, savedUserEntity.getId()),
                CREATED_SUCCESSFULLY_MSG.getValue());
    }

    private static VerifyEmailDTO prepareVerifyEmailDto(UserEntity savedUserEntity) {
        return new VerifyEmailDTO(
                savedUserEntity.getEmail(),
                savedUserEntity.getVerificationToken(),
                savedUserEntity.fullName());
    }

    private UserEntity saveNewUserToDb(CreateAccountRequest createAccountRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(createAccountRequest.firstName());
        userEntity.setLastName(createAccountRequest.lastName());
        userEntity.setEmail(createAccountRequest.email());
        userEntity.setRole(roleRepo.fetchRoleByCode(ROLE_CUSTOMER_CODE));
        return userRepo.save(userEntity);
    }

    @Transactional
    public CustomResModel verifyAccount(VerifyAccountRequest verifyEmailRequest) {
        Optional<UserEntity> userEntityOpt = findUser(verifyEmailRequest);
        return userEntityOpt.map(this::activateAccount)
                .orElseGet(this::prepareVerifyAccountFailResponse);
    }

    private Optional<UserEntity> findUser(VerifyAccountRequest verifyEmailReq) {
        return this.userRepo.findByEmailAndToken(verifyEmailReq.email(),
                verifyEmailReq.verificationToken());
    }

    private CustomResModel prepareVerifyAccountFailResponse() {
        return CustomResModel.success(VERIFY_ACCOUNT_FAIL_HTML_CONTENT,
                VERIFICATION_FAILED_RES_MSG);
    }

    private CustomResModel activateAccount(UserEntity userEntity) {
        userEntity.setVerificationToken(null);
        userEntity.setStatus(UserStatusEnum.ACTIVE);
        userRepo.save(userEntity);
        return prepareVerifyAccountSuccessResponse();
    }

    private static CustomResModel prepareVerifyAccountSuccessResponse() {
        return CustomResModel.success(VERIFY_ACCOUNT_SUCCESS_HTML_CONTENT,
                VERIFIED_SUCCESSFULLY_RES_MSG);
    }

    private void sendVerifyAccountEmail(VerifyEmailDTO verifyEmailDTO) {
        String verificationUrl = prepareVerificationUrl(verifyEmailDTO);
        MailDto mailDto = prepareMailDtoForVerifyAccount(verifyEmailDTO, verificationUrl);
        emailSender.sendInAsync(mailDto);
    }

    private static MailDto prepareMailDtoForVerifyAccount(VerifyEmailDTO verifyEmailDTO, String verificationUrl) {
        return new MailDto(
                VERIFY_ACCOUNT_EMAIL_SUBJECT,
                verifyEmailDTO.email(),
                Map.of(USER_NAME, verifyEmailDTO.username(),
                        VERIFICATION_URL, verificationUrl),
                VERIFY_ACCOUNT_EMAIL_TEMPLATE_FILE_NAME
        );
    }

    private String prepareVerificationUrl(VerifyEmailDTO verifyEmailDTO) {
        String params =
                "email=%s&token=%s".formatted(encode(verifyEmailDTO.email(), UTF_8),
                        encode(verifyEmailDTO.token(), UTF_8));
        return appProps.appUrl() + "/verify-account?" + params;
    }

    @Transactional
    public CustomResModel signIn(@Valid SignInRequest signInRequest) {

        Optional<UserEntity> user = this.userRepo.findByEmailAndDeleteFlagFalse(signInRequest.email());
        UserEntity userEntity = user.orElseThrow(() -> new EmailNotFoundException(ErrorMsgEnum.EMAIL_NOT_EXIST.getValue()));
        SendOtpDto sendOtpDto = new SendOtpDto(OtpGenerator.generateOtp(appProps.otpLength()), Instant.now().plus(20, ChronoUnit.MINUTES));
        userEntity.setOtp(sendOtpDto.otp());
        userEntity.setOtpExpiryTime(sendOtpDto.otpExpireTime());
        UserEntity updatedUser = userRepo.save(userEntity);

        MailDto mailDto = new MailDto(
                " Your One-Time Password (OTP) for Sign-In",
                updatedUser.getEmail(),
                Map.of("otp", updatedUser.getOtp(), "expireTime", "20"),
                "send-otp"
        );

        emailSender.sendInAsync(mailDto);
        return CustomResModel.success(null, "OTP sent successfully");
    }

    @Transactional
    public ResponseEntity<VerifyOtpResponse> verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        UserEntity userEntity = validateOtp(verifyOtpRequest);
        VerifyOtpResponse verifyOtpResponse = prepareVerifyOtpResponse(userEntity);
        UserEntity updatedUserEntity = updateUserEntity(userEntity);
        return prepareVerifyOtpResponseEntity(updatedUserEntity, verifyOtpResponse);
    }

    private static VerifyOtpResponse prepareVerifyOtpResponse(UserEntity userEntity) {
        VerifyOtpResponse verifyOtpResponse = new VerifyOtpResponse();
        Instant oldLastLoginTime = userEntity.getLastLoginTime();
        verifyOtpResponse.setLastLoginDetails(oldLastLoginTime);
        return verifyOtpResponse;
    }

    private ResponseEntity<VerifyOtpResponse> prepareVerifyOtpResponseEntity(UserEntity updatedUserEntity, VerifyOtpResponse verifyOtpResponse) {
        return ResponseEntity.status(HttpStatus.OK)
                .header(AUTHORIZATION, tokenHelper.generateToken(updatedUserEntity))
                .body(verifyOtpResponse);
    }

    private UserEntity updateUserEntity(UserEntity userEntity) {
        userEntity.setOtp(null);
        userEntity.setOtpExpiryTime(null);
        userEntity.setLastLoginTime(Instant.now());
        return userRepo.save(userEntity);
    }

    private UserEntity validateOtp(VerifyOtpRequest verifyOtpRequest) {
        Optional<UserEntity> userEntityOpt = findUser(verifyOtpRequest);
        UserEntity userEntity = userEntityOpt.orElseThrow(InvalidOtpException::new);
        checkForOtpExpired(userEntity);
        return userEntity;
    }

    private static void checkForOtpExpired(UserEntity userEntity) {
        if (otpIsExpiredFor(userEntity)){
            throw new OtpExpiredException();
        }
    }

    private static boolean otpIsExpiredFor(UserEntity userEntity) {
        return userEntity.getOtpExpiryTime().isAfter(Instant.now());
    }

    private Optional<UserEntity> findUser(VerifyOtpRequest verifyOtpRequest) {
        return this.userRepo.findByOtpAndEmailAndDeleteFlagFalse(verifyOtpRequest.getOtp(), verifyOtpRequest.getEmail());
    }

    public CustomResModel verifyJwtToken(String authToken){
      return  CustomResModel.success(tokenHelper.validateJwtToken(authToken),"token is valid");
    }
}



record VerifyEmailDTO(String email, String token, String username) {
}


