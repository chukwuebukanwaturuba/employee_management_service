package com.ebuka.employeemanagementsysytem.controller;

import com.ebuka.employeemanagementsysytem.model.dto.response.ApiResponse;
import com.ebuka.employeemanagementsysytem.model.dto.response.EmployeeProfileResponse;
import com.ebuka.employeemanagementsysytem.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final EmployeeService employeeService;

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> getMyProfile() {
        EmployeeProfileResponse profile = employeeService.getCurrentUserProfile();
        return ResponseEntity.ok(new ApiResponse<>(profile, "Profile retrieved successfully"));
    }
}
