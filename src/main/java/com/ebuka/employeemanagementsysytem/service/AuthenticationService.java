package com.ebuka.employeemanagementsysytem.service;


import com.ebuka.employeemanagementsysytem.exception.EMPLOYEEAPPException;
import com.ebuka.employeemanagementsysytem.model.dto.request.LoginRequest;
import com.ebuka.employeemanagementsysytem.model.dto.response.LoginResponse;

public interface AuthenticationService {

    LoginResponse authenticateUser(LoginRequest request) throws EMPLOYEEAPPException;

//    String resetPassword(ChangePasswordRequest request);

//    void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException;
}
