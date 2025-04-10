package com.ebuka.employeemanagementsysytem.service.impl;

import com.ebuka.employeemanagementsysytem.configuration.rabbitmq.RabbitMQConfig;
import com.ebuka.employeemanagementsysytem.enums.Department;
import com.ebuka.employeemanagementsysytem.enums.Gender;
import com.ebuka.employeemanagementsysytem.enums.Role;
import com.ebuka.employeemanagementsysytem.exception.EMPLOYEEAPPException;
import com.ebuka.employeemanagementsysytem.exception.ErrorStatus;
import com.ebuka.employeemanagementsysytem.model.dto.ResponseDto;
import com.ebuka.employeemanagementsysytem.model.dto.request.EmployeeUpdateRequest;
import com.ebuka.employeemanagementsysytem.model.dto.request.MailRequest;
import com.ebuka.employeemanagementsysytem.model.dto.request.RegisterRequest;
import com.ebuka.employeemanagementsysytem.model.dto.response.EmployeeProfileResponse;
import com.ebuka.employeemanagementsysytem.model.dto.response.RegisterResponse;
import com.ebuka.employeemanagementsysytem.model.entity.Users;
import com.ebuka.employeemanagementsysytem.repository.UserRepository;
import com.ebuka.employeemanagementsysytem.service.AdminService;
import com.ebuka.employeemanagementsysytem.utils.IdGeneratorUtil;
import com.ebuka.employeemanagementsysytem.utils.LogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

import static com.ebuka.employeemanagementsysytem.enums.Department.HR;
import static com.ebuka.employeemanagementsysytem.enums.Gender.FEMALE;
import static com.ebuka.employeemanagementsysytem.enums.Gender.MALE;
import static com.ebuka.employeemanagementsysytem.enums.Role.ADMIN;import static com.ebuka.employeemanagementsysytem.enums.Status.ACTIVE;
import static com.ebuka.employeemanagementsysytem.enums.Status.DELETED;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //@Autowired
    private final RabbitTemplate rabbitTemplate;

    //@Autowired
    private final ObjectMapper objectMapper;

    //@Autowired
    private final Environment environment;
    @Override
    public RegisterResponse registerAdmin(RegisterRequest registerRequest) {

        LogUtils.infoLog("register CustomerProfile started");
        LogUtils.infoLog("Login user request: email > " + registerRequest.getEmail() + " Gender > " + registerRequest.getGender());
        Optional<Users> existingAdminOp = userRepository.findUsersByEmailOrPhoneNumber(registerRequest.getEmail(), registerRequest.getPhoneNumber());
        Users existingAdmin;

        if (existingAdminOp.isPresent()) {
            existingAdmin = existingAdminOp.get();
            if (existingAdmin.getStatus().equals(DELETED)) {
                throw new EMPLOYEEAPPException(ErrorStatus.USER_ALL_EXISTS_ERROR);
            } else if (existingAdmin.getRole().equals(ADMIN)) {
                LogUtils.infoLog("is customer present?, " + existingAdminOp.isPresent());
                throw new EMPLOYEEAPPException(ErrorStatus.USER_ALL_EXISTS_ERROR);
            }
        }
        Users users = createAdmin(registerRequest);


        RegisterResponse.UsersDto userDto = new RegisterResponse.UsersDto(users);
        log.info("Register CustomerProfile ended");
        ResponseDto responseDto = new ResponseDto();
        return new RegisterResponse("CustomerProfile saved", userDto, responseDto);
    }

    @Override
    public RegisterResponse addEmployee(@NonNull RegisterRequest request) {
        LogUtils.infoLog("Add Employee started");
        LogUtils.infoLog("Add Employee request: email > " + request.getEmail() + " role > " + request.getRole());

        Optional<Users> existingUserOp = userRepository.findUsersByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber());

        if (existingUserOp.isPresent()) {
            Users existingUser = existingUserOp.get();
            if (!existingUser.getStatus().equals(DELETED)) {
                throw new EMPLOYEEAPPException(ErrorStatus.USER_ALL_EXISTS_ERROR);
            }
        }

        Users currentUser = getCurrentUser();
        if (currentUser.getRole() != ADMIN) {
            throw new EMPLOYEEAPPException(ErrorStatus.UNAUTHORIZED_OPERATION);
        }

        Users employee = mapToUser(request);
        employee.setRole(Role.valueOf(request.getRole().toUpperCase()));
        employee.setDepartment(Department.valueOf(request.getDepartment().toUpperCase()));
        employee.setStatus(ACTIVE);
        employee.setEnabled(false);
        employee.setVerified(false);

        Users savedEmployee = userRepository.save(employee);

        // Publish to RabbitMQ
        MailRequest mailRequest = new MailRequest(
                savedEmployee.getEmail(),
                "Welcome to Our Platform",
                "Dear " + savedEmployee.getFirstName() + ",\n\n" +
                        "Your account has been created successfully.\n" +
                        "Email: " + savedEmployee.getEmail() + "\n" +
                        "Password: " + request.getPassword()
        );


        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY,
                    objectMapper.writeValueAsString(mailRequest)
            );
        } catch (JsonProcessingException e) {
            log.error("Failed to convert MailRequest to JSON: {}", e.getMessage());
            throw new RuntimeException("Failed to send email message to RabbitMQ", e);
        }

        RegisterResponse.UsersDto userDto = new RegisterResponse.UsersDto(savedEmployee);
        log.info("Add Employee ended");
        ResponseDto responseDto = new ResponseDto();
        return new RegisterResponse("Employee added successfully", userDto, responseDto);
    }

    private Users createAdmin(RegisterRequest request) {
        Users user = mapToUser(request);
        Users savedAdmin = userRepository.save(user);
        return savedAdmin;
    }

    private Users mapToUser(RegisterRequest request) {
        log.info("mapToVendor");
        Users admin = new Users();
        String employeeId;
        do {
            employeeId = IdGeneratorUtil.generateEmployeeId();
        } while (userRepository.existsByEmployeeId(employeeId));
        admin.setEmployeeId(employeeId);
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setEnabled(false);
        admin.setRole(ADMIN);
        admin.setDepartment(HR);
        admin.setStatus(ACTIVE);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setVerified(false);
        if (request.getGender().equalsIgnoreCase("female")) {
            admin.setGender(FEMALE);
        } else {
            admin.setGender(MALE);
        }
        return admin;
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EMPLOYEEAPPException(ErrorStatus.USER_NOT_FOUND));
    }

    @Override
    public EmployeeProfileResponse updateEmployee(Long employeeId, EmployeeUpdateRequest updateRequest) {
        Users currentAdmin = getCurrentUser();
        if (currentAdmin.getRole() != ADMIN) {
            throw new EMPLOYEEAPPException(ErrorStatus.UNAUTHORIZED_OPERATION);
        }

        Users employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EMPLOYEEAPPException(ErrorStatus.USER_NOT_FOUND));

        if (updateRequest.getFirstName() != null) {
            employee.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            employee.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getPhoneNumber() != null) {
            employee.setPhoneNumber(updateRequest.getPhoneNumber());
        }
        if (updateRequest.getDepartment() != null) {
            employee.setDepartment(Department.valueOf(updateRequest.getDepartment().toUpperCase()));
        }
        if (updateRequest.getRole() != null) {
            employee.setRole(Role.valueOf(updateRequest.getRole().toUpperCase()));
        }
        if (updateRequest.getGender() != null) {
            employee.setGender(Gender.valueOf(updateRequest.getGender().toUpperCase()));
        }

        Users updatedEmployee = userRepository.save(employee);

        return EmployeeProfileResponse.builder()
                .firstName(updatedEmployee.getFirstName())
                .lastName(updatedEmployee.getLastName())
                .email(updatedEmployee.getEmail())
                .phoneNumber(updatedEmployee.getPhoneNumber())
                .employeeId(updatedEmployee.getEmployeeId())
                .department(updatedEmployee.getDepartment().name())
                .role(updatedEmployee.getRole().name())
                .gender(updatedEmployee.getGender().name())
                .build();
    }

    @Override
    public String deleteEmployee(Long employeeId) {
        Users currentAdmin = getCurrentUser();
        if (currentAdmin.getRole() != ADMIN) {
            throw new EMPLOYEEAPPException(ErrorStatus.UNAUTHORIZED_OPERATION);
        }

        Users employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EMPLOYEEAPPException(ErrorStatus.USER_NOT_FOUND));

        if (employee.getRole() == ADMIN) {
            throw new EMPLOYEEAPPException(ErrorStatus.CANNOT_DELETE_ADMIN);
        }

        employee.setStatus(DELETED);
        userRepository.save(employee);

        return "Employee with ID " + employeeId + " has been deleted successfully";
    }

    @Override
    public String reactivateEmployee(Long employeeId) {
        Users currentAdmin = getCurrentUser();
        if (currentAdmin.getRole() != ADMIN) {
            throw new EMPLOYEEAPPException(ErrorStatus.UNAUTHORIZED_OPERATION);
        }

        Users employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EMPLOYEEAPPException(ErrorStatus.USER_NOT_FOUND));

        if (employee.getStatus() != DELETED) {
            throw new EMPLOYEEAPPException(ErrorStatus.USER_NOT_DELETED);
        }

        employee.setStatus(ACTIVE);
        employee.setEnabled(false);
        employee.setVerified(false);
        userRepository.save(employee);

        sendReactivationNotification(employee);

        return "Employee with ID " + employeeId + " has been reactivated successfully";
    }

    private void sendReactivationNotification(Users employee) {
        MailRequest mailRequest = new MailRequest(
                employee.getEmail(),
                "Account Reactivation Notice",
                "Dear " + employee.getFirstName() + ",\n\n" +
                        "Your account has been reactivated by the administrator.\n" +
                        "You can now login to the system with your credentials.\n\n" +
                        "Best regards,\n" +
                        "The Admin Team"
        );

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY,
                    objectMapper.writeValueAsString(mailRequest)
            );
        } catch (JsonProcessingException e) {
            log.error("Failed to send reactivation email: {}", e.getMessage());
        }
    }
}
