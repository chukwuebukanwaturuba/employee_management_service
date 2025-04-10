package com.ebuka.employeemanagementsysytem.model.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String gender;
    private String role;
    private String department;
}
