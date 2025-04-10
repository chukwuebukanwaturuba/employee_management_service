package com.ebuka.employeemanagementsysytem.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

@Slf4j
public class UserUtils {

    public static UserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (!Objects.isNull(principal)) {
            return (UserDetails) principal;
        }
        return null;
    }
}
