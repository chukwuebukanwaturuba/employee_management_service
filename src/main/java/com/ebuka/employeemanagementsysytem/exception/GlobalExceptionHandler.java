package com.ebuka.employeemanagementsysytem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EMPLOYEEAPPException.class)
    public ResponseEntity<ErrorResponse> handleEMPLOYEEAPPException(EMPLOYEEAPPException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorMessage(), ex.getErrorStatus().getDefaultMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatusCode()));
    }
}

class ErrorResponse {
    private String errorMessage;
    private String description;

    public ErrorResponse(String errorMessage, String description) {
        this.errorMessage = errorMessage;
        this.description = description;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
