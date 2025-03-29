package com.amalvadkar.lms.auth.app.constants;

public class AppConstants {

    public static final String OTP_VERIFIED_SUCCESSFULLY_MSG = "OTP Verified successfully";

    private AppConstants() {
        throw new AssertionError("No com.amalvadkar.lms.auth.app.constants.AppConstants instances for you!");
    }

    public static final String ROLE_CUSTOMER_CODE = "CUSTOMER";
    public static final String ROLE_NOT_FOUND_ERR_MSG = "Role not found";
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
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "username";
    public static final String VERIFICATION_URL = "verificationUrl";
    public static final String OTP_SENT_SUCCESSFULLY_MSG = "OTP sent successfully";
    public static final String REQUEST_HEADER_DEVICE = "device";
    public static final String TOKEN_VERIFIED_SUCCESSFULLY_MSG = "Token verified successfully";
}
