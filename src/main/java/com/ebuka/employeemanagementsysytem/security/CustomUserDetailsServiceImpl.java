package com.ebuka.employeemanagementsysytem.security;

import com.ebuka.employeemanagementsysytem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return userRepository.findUsersByEmailOrPhoneNumber(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
    }
}
