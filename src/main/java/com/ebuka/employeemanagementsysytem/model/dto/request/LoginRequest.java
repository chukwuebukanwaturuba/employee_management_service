package com.ebuka.employeemanagementsysytem.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Password cannot be blank")
    private String emailOrPhoneNumber;

    @NotBlank(message = "Password cannot be blank")
    private String password;

}