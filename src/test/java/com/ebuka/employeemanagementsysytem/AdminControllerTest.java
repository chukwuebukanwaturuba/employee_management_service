package com.ebuka.employeemanagementsysytem;

import com.ebuka.employeemanagementsysytem.controller.AdminController;
import com.ebuka.employeemanagementsysytem.model.dto.request.EmployeeUpdateRequest;
import com.ebuka.employeemanagementsysytem.model.dto.response.ApiResponse;
import com.ebuka.employeemanagementsysytem.model.dto.response.EmployeeProfileResponse;
import com.ebuka.employeemanagementsysytem.service.AdminService;
import com.ebuka.employeemanagementsysytem.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Mock
    private AdminService adminService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void getEmployeeProfile_ShouldReturnProfile_WhenAdminAccess() {
        // Arrange
        Long userId = 1L;
        EmployeeProfileResponse mockResponse = EmployeeProfileResponse.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(employeeService.getEmployeeProfile(userId)).thenReturn(mockResponse);

        ResponseEntity<ApiResponse<EmployeeProfileResponse>> response =
                adminController.getProfile(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getData().getFirstName());
        verify(employeeService).getEmployeeProfile(userId);
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedProfile_WhenAdminAccess() {
        // Arrange
        Long employeeId = 1L;
        EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest();
        updateRequest.setFirstName("UpdatedName");

        EmployeeProfileResponse mockResponse = EmployeeProfileResponse.builder()
                .firstName("UpdatedName")
                .build();

        when(adminService.updateEmployee(employeeId, updateRequest)).thenReturn(mockResponse);

        ResponseEntity<ApiResponse<EmployeeProfileResponse>> response =
                adminController.updateEmployee(employeeId, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UpdatedName", response.getBody().getData().getFirstName());
        verify(adminService).updateEmployee(employeeId, updateRequest);
    }




}
