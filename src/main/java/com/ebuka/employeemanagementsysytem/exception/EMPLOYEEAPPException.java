package com.ebuka.employeemanagementsysytem.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EMPLOYEEAPPException extends RuntimeException {

    private String errorMessage;
    private ErrorStatus errorStatus;
    private int statusCode;
    public EMPLOYEEAPPException(ErrorStatus errorStatus, String errorMessage) {
        this.errorStatus = errorStatus;
        this.errorMessage = errorMessage;
        this.statusCode = errorStatus.getErrorCode();
    }
    public EMPLOYEEAPPException(ErrorStatus errorStatus){
        this.errorStatus = errorStatus;
        this.errorMessage = errorStatus.getDefaultMessage();
        this.statusCode = errorStatus.getErrorCode();
    }
}
