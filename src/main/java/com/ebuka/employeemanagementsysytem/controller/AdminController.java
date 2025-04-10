package com.ebuka.employeemanagementsysytem.controller;

import com.ebuka.employeemanagementsysytem.exception.EMPLOYEEAPPException;
import com.ebuka.employeemanagementsysytem.model.dto.request.EmployeeUpdateRequest;
import com.ebuka.employeemanagementsysytem.model.dto.request.RegisterRequest;
import com.ebuka.employeemanagementsysytem.model.dto.response.ApiResponse;
import com.ebuka.employeemanagementsysytem.model.dto.response.EmployeeProfileResponse;
import com.ebuka.employeemanagementsysytem.model.dto.response.RegisterResponse;
import com.ebuka.employeemanagementsysytem.service.AdminService;
import com.ebuka.employeemanagementsysytem.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {
    private final AdminService adminService;
    private final EmployeeService employeeService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<RegisterResponse>> RegisterAdmin(@RequestBody @Valid @NonNull RegisterRequest request) throws EMPLOYEEAPPException {
        return ResponseEntity.ok(new ApiResponse<>(adminService.registerAdmin(request)));
    }

    @PostMapping("/add-employee")
    public ResponseEntity<ApiResponse<RegisterResponse>> addEmployee(@RequestBody @Valid @NonNull RegisterRequest request) throws EMPLOYEEAPPException {
        return ResponseEntity.ok(new ApiResponse<>(adminService.addEmployee(request)));
    }


    @GetMapping("/employee-profile/{userId}")
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> getProfile(@PathVariable Long userId){
        EmployeeProfileResponse profile = employeeService.getEmployeeProfile(userId);
        return ResponseEntity.ok(new ApiResponse<>(profile, "Employee profile retrieved successfully"));
    }

    @PutMapping("/update-employee/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeUpdateRequest updateRequest) {
        EmployeeProfileResponse updatedProfile = adminService.updateEmployee(employeeId, updateRequest);
        return ResponseEntity.ok(new ApiResponse<>(updatedProfile, "Employee updated successfully"));
    }

    @DeleteMapping("/delete-employee/{employeeId}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable Long employeeId) {
        String message = adminService.deleteEmployee(employeeId);
        return ResponseEntity.ok(new ApiResponse<>(message));
    }

    @PatchMapping("/reactivate-employee/{employeeId}")
    public ResponseEntity<ApiResponse<String>> reactivateEmployee(@PathVariable Long employeeId) {
        String message = adminService.reactivateEmployee(employeeId);
        return ResponseEntity.ok(new ApiResponse<>(message));
    }
}




