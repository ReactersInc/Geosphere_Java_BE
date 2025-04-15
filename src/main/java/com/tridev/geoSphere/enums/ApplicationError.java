package com.tridev.geoSphere.enums;


import com.tridev.geoSphere.constant.CommonValidationConstant;
import org.springframework.lang.Nullable;


public enum ApplicationError {
	
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    UN_AUTHORISED(401, "Unauthorised"),
    FILE_UPLOAD_FAILURE(100001, CommonValidationConstant.FILE_UPLOAD_FAILURE_EXCEPTION),
    INVALID_REQUEST(100002, CommonValidationConstant.INVALID_REQUEST),
    INVALID_PHONENO(100003, CommonValidationConstant.INVALID_PHONENO),
    INVALID_NAME(100004, CommonValidationConstant.INVALID_NAME),
    INVALID_EMAIL(100005,CommonValidationConstant.INVALID_EMAIL),
    RESET_PASSWORD_ERROR(100006, CommonValidationConstant.RESET_PASSWORD_ERROR),
    CURRENT_PASSWORD_MISMATCH(100007, CommonValidationConstant.CURRENT_PASSWORD_MISMATCH),

    //__________________________________________//
    CURRENT_PASSWORD_IS_REQUIRED(100008, CommonValidationConstant.CURRENT_PASSWORD_IS_REQUIRED),
    NEW_PASSWORD_IS_REQUIRED(100009, CommonValidationConstant.NEW_PASSWORD_IS_REQUIRED),
    FULLNAME_IS_MANDATORY(100010, CommonValidationConstant.FULLNAME_IS_MANDATORY),
    EMAIL_IS_MANDATORY(100011, CommonValidationConstant.EMAIL_IS_MANDATORY),
    PASSWORD_IS_MANDATORY(100012, CommonValidationConstant.PASSWORD_IS_MANDATORY),
    USER_ALREADY_EXISTS(100013, CommonValidationConstant.USER_ALREADY_EXISTS),
    INCORRECT_OTP(100014, CommonValidationConstant.INCORRECT_OTP),
    ACCOUNT_NOT_FOUND(100015, CommonValidationConstant.ACCOUNT_NOT_FOUND),
    INVALID_FCM_TOKEN(100016, CommonValidationConstant.INVALID_FCM_TOKEN),
    ID_IS_MANDATORY(100017,CommonValidationConstant.ID_IS_MANDATORY),
    CUSTOMER_NOT_FOUND(100018, CommonValidationConstant.CUSTOMER_NOT_FOUND),
    OTP_EXPIRED(100019, CommonValidationConstant.OTP_EXPIRED),
    EMPTY_USER_LIST(100020, CommonValidationConstant.EMPTY_USER_LIST),
    PUSH_NOTIFICATION_FAILED(100021, CommonValidationConstant.PUSH_NOTIFICATION_FAILED),
    USER_NOT_FOUND(100022,CommonValidationConstant.USER_NOT_FOUND),
    USER_NOT_VERIFIED(100023,CommonValidationConstant.USER_NOT_VERIFIED),
    BAD_REQUEST(100023,CommonValidationConstant.BAD_REQUEST),
    GEOFENCE_NOT_FOUND(100024,CommonValidationConstant.GEOFENCE_NOT_FOUND),
    GEOFENCE_ALREADY_EXIST(100025, CommonValidationConstant.GEOFENCE_ALREADY_EXIST );
    private final int code;
    private final String reason;
    ApplicationError(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    public static int getCodeByReason(String reason) {
        for (ApplicationError status : values()) {
            if (status.reason.equals(reason)) {
                return status.code;
            }
        }
        return 0;
    }

    public static ApplicationError getApplicationErrorByReason(String reason) {
        for (ApplicationError status : values()) {
            if (status.reason.equals(reason)) {
                return status;
            }
        }
        return null;
    }


    public String getCodeString() {
        return String.valueOf(code);
    }

    @Nullable
    public static ApplicationError resolve(int statusCode) {
        for (ApplicationError status : values()) {
            if (status.code == statusCode) {
                return status;
            }
        }
        return null;
    }
}
