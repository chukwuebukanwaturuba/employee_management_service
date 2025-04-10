package com.ebuka.employeemanagementsysytem.service.impl;

import com.ebuka.employeemanagementsysytem.enums.Role;
import com.ebuka.employeemanagementsysytem.model.dto.response.EmployeeProfileResponse;
import com.ebuka.employeemanagementsysytem.model.entity.Users;
import com.ebuka.employeemanagementsysytem.repository.UserRepository;
import com.ebuka.employeemanagementsysytem.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final UserRepository userRepository;


    @Override
    public EmployeeProfileResponse getEmployeeProfile(Long employeeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Users currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.MANAGER) {
            throw new RuntimeException("Unauthorized: Only ADMIN or MANAGER can access employee profiles");
        }

        Users user = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        return EmployeeProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .employeeId(user.getEmployeeId())
                .department(user.getDepartment().name())
                .role(user.getRole().name())
                .gender(user.getGender().name())
                .build();
    }


    @Override
    public EmployeeProfileResponse getCurrentUserProfile() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> EmployeeProfileResponse.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .employeeId(user.getEmployeeId())
                        .department(user.getDepartment().name())
                        .role(user.getRole().name())
                        .gender(user.getGender().name())
                        .build())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
