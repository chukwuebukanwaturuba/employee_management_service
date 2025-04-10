package com.ebuka.employeemanagementsysytem.configuration.security;

import com.ebuka.employeemanagementsysytem.security.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigFilter {
    private final JWTAuthenticationFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;




    private final String[] WHITE_LIST = new String[]{
            "/v3/api-docs.yaml/",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/password/**",
              "/api/v1/auth/login",
            "/api/v1/forgot-password/**",
            "/api/v1/admin/register"
    };





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info(" =====>> Jwt filter is value: {} <=====", jwtFilter);
        return http
                .cors((httpSecurityCorsConfigurer) -> corsConfigurationSource())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
