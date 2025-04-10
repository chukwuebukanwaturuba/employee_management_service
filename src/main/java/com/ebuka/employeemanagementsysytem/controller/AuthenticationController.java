package com.ebuka.employeemanagementsysytem.controller;

import com.ebuka.employeemanagementsysytem.exception.EMPLOYEEAPPException;
import com.ebuka.employeemanagementsysytem.model.dto.request.LoginRequest;
import com.ebuka.employeemanagementsysytem.model.dto.response.ApiResponse;
import com.ebuka.employeemanagementsysytem.model.dto.response.LoginResponse;
import com.ebuka.employeemanagementsysytem.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request)
            throws EMPLOYEEAPPException {
        return ResponseEntity.ok(new ApiResponse<>(authenticationService.authenticateUser(request)));
    }
}
