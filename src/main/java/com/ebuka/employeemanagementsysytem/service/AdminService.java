package com.ebuka.employeemanagementsysytem.service;

import com.ebuka.employeemanagementsysytem.model.dto.request.EmployeeUpdateRequest;
import com.ebuka.employeemanagementsysytem.model.dto.request.RegisterRequest;
import com.ebuka.employeemanagementsysytem.model.dto.response.EmployeeProfileResponse;
import com.ebuka.employeemanagementsysytem.model.dto.response.RegisterResponse;
import jakarta.validation.Valid;
import lombok.NonNull;

public interface AdminService {
    RegisterResponse registerAdmin(RegisterRequest registerRequest);

    RegisterResponse addEmployee(@Valid @NonNull RegisterRequest request);
    EmployeeProfileResponse updateEmployee(Long employeeId, EmployeeUpdateRequest updateRequest);
    String deleteEmployee(Long employeeId);
    String reactivateEmployee(Long employeeId);
}
