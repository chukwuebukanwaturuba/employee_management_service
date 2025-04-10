package com.ebuka.employeemanagementsysytem.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String userID;
    private String accessToken;
    private String userType;
    private String username;
    private String employeeId;
    private Date expiredAt;
    private String fullName;
}