package com.ebuka.employeemanagementsysytem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus {

    VALIDATION_ERROR(400, "Validation Error"),
    VALIDATION_ERROR_TRF(400,"This request has either been approved or declined and cannot be treated again" ),
    INCOMPLETE_REGISTRATION_ERROR(403, "incomplete Registration"),
    ADDRESS_ALREADY_EXISTING(400, "Address already exist"),
    INVALID_INPUT_ERROR(400, "Invalid input"),
    RESOURCE_NOT_FOUND(404, "Resource not found"),
    USER_ALL_EXISTS_ERROR(409, "User already exists"),
    PASSWORD_CONFLICT(400, "Password does not match"),
    UNAUTHORIZED(402, "Unauthorized request"),
    OTP_EXPIRED(410, "otp expired"),
    GENERAL_ERROR(500, "oops something went wrong"),
    INSUFFICIENT_FUNDS(408, "insufficient funds" ),
    INVALID_INPUT(400,"invalid order status" ),
    ACCOUNT_NOT_FOUND_ERROR(400,"account not found" ),
    UNAUTHORIZED_OPERATION(403,"User not an admin, cannot access this resource" ),
//    UNAUTHORIZED_OPERATION("Unauthorized operation", HttpStatus.FORBIDDEN),
    CANNOT_DELETE_ADMIN(403,"Cannot delete another admin"),
    USER_NOT_FOUND(404,"User not found"),
    USER_NOT_DELETED(400,"User is not in deleted state");


    private final int errorCode;
    private final String defaultMessage;

    ErrorStatus(int errorCode, String defaultMessage){
        this.defaultMessage = defaultMessage;
        this.errorCode = errorCode;
    }
}
