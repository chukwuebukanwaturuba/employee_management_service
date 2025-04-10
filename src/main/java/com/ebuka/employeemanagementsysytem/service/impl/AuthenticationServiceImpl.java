package com.ebuka.employeemanagementsysytem.service.impl;


import com.ebuka.employeemanagementsysytem.exception.EMPLOYEEAPPException;
import com.ebuka.employeemanagementsysytem.exception.ErrorStatus;
import com.ebuka.employeemanagementsysytem.model.dto.request.LoginRequest;
import com.ebuka.employeemanagementsysytem.model.dto.response.LoginResponse;
import com.ebuka.employeemanagementsysytem.model.entity.Users;
import com.ebuka.employeemanagementsysytem.repository.UserRepository;
import com.ebuka.employeemanagementsysytem.security.JwtService;
import com.ebuka.employeemanagementsysytem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.ebuka.employeemanagementsysytem.enums.Status.ACTIVE;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;



        @Transactional
        @Override
        public LoginResponse authenticateUser(LoginRequest request) throws EMPLOYEEAPPException {
            String identifier = request.getEmailOrPhoneNumber();
            Users users = userRepository.findUsersByEmailOrPhoneNumber(identifier, identifier)
                    .orElseThrow(() -> new EMPLOYEEAPPException(ErrorStatus.ACCOUNT_NOT_FOUND_ERROR));
            if(!passwordEncoder.matches(request.getPassword(), users.getPassword())){
                throw new EMPLOYEEAPPException(ErrorStatus.PASSWORD_CONFLICT);
            }
            validateUser(users);
            users.setEnabled(true);
            users.setVerified(true);

            userRepository.save(users);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            identifier,
                            request.getPassword()
                    )
            );


            String jwtToken = jwtService.generateTokenForCustomer(users);
            LoginResponse response = createLoginResponse(users, jwtToken);
            System.out.println("here=========");
            return response;
        }


        private void validateUser(Users users) throws EMPLOYEEAPPException{
            if (users.getStatus() != ACTIVE) {
                throw new EMPLOYEEAPPException(ErrorStatus.INCOMPLETE_REGISTRATION_ERROR);
            }

        }



        public LoginResponse createLoginResponse(Users users, String jwtToken) {
            return LoginResponse.builder()
                    .userID(Objects.requireNonNull(users.getId()).toString())
                    .accessToken(jwtToken)
                    .username(users.getFirstName())
                    .fullName(users.getFirstName() + " " + users.getLastName())
                    .employeeId(users.getEmployeeId())
                    .expiredAt(jwtService.extractExpiration(jwtToken))
                    .userType(String.valueOf(users.getRole()))
                    .build();
        }

}