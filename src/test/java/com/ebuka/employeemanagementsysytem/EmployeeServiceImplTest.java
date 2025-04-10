package com.ebuka.employeemanagementsysytem;

import com.ebuka.employeemanagementsysytem.enums.Department;
import com.ebuka.employeemanagementsysytem.enums.Gender;
import com.ebuka.employeemanagementsysytem.enums.Role;
import com.ebuka.employeemanagementsysytem.model.dto.response.EmployeeProfileResponse;
import com.ebuka.employeemanagementsysytem.model.entity.Users;
import com.ebuka.employeemanagementsysytem.repository.UserRepository;
import com.ebuka.employeemanagementsysytem.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;



    @Test
    void getCurrentUserProfile_ShouldReturnProfile_WhenUserAuthenticated() {
        // Arrange
        String email = "test@example.com";
        Users mockUser = Users.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email(email)
                .department(Department.HR)
                .role(Role.MANAGER)
                .gender(Gender.FEMALE)
                .build();

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        EmployeeProfileResponse response = employeeService.getCurrentUserProfile();

        assertNotNull(response);
        assertEquals("Test", response.getFirstName());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getCurrentUserProfile_ShouldThrowException_WhenUserNotFound() {
        String email = "nonexistent@example.com";

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.getCurrentUserProfile();
        });

        assertTrue(exception.getMessage().contains("User not found")); // Adjust message as needed
        verify(userRepository, times(1)).findByEmail(email);
    }
}