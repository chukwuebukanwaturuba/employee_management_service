package com.ebuka.employeemanagementsysytem.service;

import com.ebuka.employeemanagementsysytem.model.dto.response.EmployeeProfileResponse;

public interface EmployeeService {

    EmployeeProfileResponse getEmployeeProfile(Long employeeId);
    EmployeeProfileResponse getCurrentUserProfile();
}
