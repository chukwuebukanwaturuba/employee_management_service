package com.ebuka.employeemanagementsysytem.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String department;
    private String role;
    private String gender;
}
