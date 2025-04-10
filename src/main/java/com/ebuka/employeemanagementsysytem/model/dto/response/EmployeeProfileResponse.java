package com.ebuka.employeemanagementsysytem.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String employeeId;
    private String department;
    private String role;
    private String gender;
}
