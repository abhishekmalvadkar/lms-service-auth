package com.amalvadkar.lms.auth.app.services;

import com.amalvadkar.lms.auth.ApplicationProperties;
import com.amalvadkar.lms.auth.app.constants.AppConstants;
import com.amalvadkar.lms.auth.app.entities.UserEntity;
import com.amalvadkar.lms.auth.app.enums.UserStatusEnum;
import com.amalvadkar.lms.auth.app.exception.AccountLockedException;
import com.amalvadkar.lms.auth.app.exception.InvalidOtpException;
import com.amalvadkar.lms.auth.app.exception.OtpExpiredException;
import com.amalvadkar.lms.auth.app.generator.OtpGenerator;
import com.amalvadkar.lms.auth.app.helper.TokenHelper;
import com.amalvadkar.lms.auth.app.models.dto.CreateTokenDto;
import com.amalvadkar.lms.auth.app.models.dto.OtpDto;
import com.amalvadkar.lms.auth.app.models.request.*;
import com.amalvadkar.lms.auth.app.models.resonse.CustomResModel;
import com.amalvadkar.lms.auth.app.models.resonse.VerifyOtpResponse;
import com.amalvadkar.lms.auth.app.models.resonse.VerifyTokenResponse;
import com.amalvadkar.lms.auth.app.repositories.RoleRepo;
import com.amalvadkar.lms.auth.app.repositories.TagRepo;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amalvadkar.lms.auth.app.constants.AppConstants.*;
import static com.amalvadkar.lms.auth.app.enums.MetaDataEnum.TAG_DROP_DOWN_OPTIONS;
import static com.amalvadkar.lms.auth.app.enums.ResponseMessageEnum.CREATED_SUCCESSFULLY;
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
    private final OtpGenerator otpGenerator;
    private final TagRepo tagRepo;

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
                CREATED_SUCCESSFULLY.getValue());
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
        UserEntity userEntity = userRepo.findUserOrThrow(signInRequest.email());
        checkForAccountLocked(userEntity);
        UserEntity updatedUser = updateUserWithOtp(userEntity);
        sendOtpEmail(updatedUser);
        return CustomResModel.success(OTP_SENT_SUCCESSFULLY_MSG);
    }

    private void sendOtpEmail(UserEntity updatedUser) {
        MailDto mailDto = prepareOtpMailDto(updatedUser);
        emailSender.sendInAsync(mailDto);
    }

    private MailDto prepareOtpMailDto(UserEntity updatedUser) {
        return new MailDto(
                "Your One-Time Password (OTP) for Sign-In",
                updatedUser.getEmail(),
                Map.of("otp", updatedUser.getOtp(), "expireTime", appProps.otpExpiryDurationInMin()),
                "send-otp"
        );
    }

    private UserEntity updateUserWithOtp(UserEntity userEntity) {
        OtpDto otpDto = otpGenerator.generate();
        userEntity.setOtp(otpDto.otp());
        userEntity.setOtpExpiryTime(otpDto.otpExpiryTime());
        return userRepo.save(userEntity);
    }

    private void checkForAccountLocked(UserEntity userEntity) {
        if (userEntity.isAccountLocked()) {
            throw new AccountLockedException();
        }
    }

    @Transactional
    public ResponseEntity<VerifyOtpResponse> verifyOtp(VerifyOtpRequest verifyOtpRequest, String device) {
        UserEntity userEntity = validateOtp(verifyOtpRequest);
        UserEntity updatedUserEntity = updateUserEntity(userEntity);
        VerifyOtpResponse verifyOtpResponse = prepareVerifyOtpResponse(updatedUserEntity);
        String token = generateJwtToken(device, updatedUserEntity);
        return prepareVerifyOtpResponseEntity(verifyOtpResponse, token);
    }

    private Map<String, Object> prepareMetadata(UserEntity updatedUserEntity) {
        Map<String, Object> metaData = new HashMap<>();
        metaData.put(TAG_DROP_DOWN_OPTIONS.value(), tagRepo.findTagsForUser(updatedUserEntity.getId()));
        return metaData;
    }

    private String generateJwtToken(String device, UserEntity userEntity) {
        CreateTokenDto createTokenDto = new CreateTokenDto(userEntity.getId(),
                userEntity.getRole().getId(), device);
        return tokenHelper.generate(createTokenDto);
    }

    private VerifyOtpResponse prepareVerifyOtpResponse(UserEntity userEntity) {
        VerifyOtpResponse verifyOtpResponse = new VerifyOtpResponse();
        Instant oldLastLoginTime = userEntity.getLastLoginTime();
        verifyOtpResponse.setLastLoginDetails(oldLastLoginTime);
        verifyOtpResponse.setMetaData(prepareMetadata(userEntity));
        return verifyOtpResponse;
    }

    private ResponseEntity<VerifyOtpResponse> prepareVerifyOtpResponseEntity(VerifyOtpResponse verifyOtpResponse, String token) {
        return ResponseEntity.status(HttpStatus.OK)
                .header(AUTHORIZATION, token)
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
        return userEntity.getOtpExpiryTime().isBefore(Instant.now());
    }

    private Optional<UserEntity> findUser(VerifyOtpRequest verifyOtpRequest) {
        return this.userRepo.findByOtpAndEmailAndDeleteFlagFalse(verifyOtpRequest.otp(), verifyOtpRequest.email());
    }

    public CustomResModel verifyToken(VerifyTokenRequest verifyTokenRequest){
        VerifyTokenResponse verifyTokenResponse = tokenHelper.verify(verifyTokenRequest.token());
        return  CustomResModel.success(verifyTokenResponse, TOKEN_VERIFIED_SUCCESSFULLY_MSG);
    }
}



record VerifyEmailDTO(String email, String token, String username) {
}


