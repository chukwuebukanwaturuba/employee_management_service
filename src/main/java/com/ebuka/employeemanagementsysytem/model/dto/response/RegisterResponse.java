package com.ebuka.employeemanagementsysytem.model.dto.response;

import com.ebuka.employeemanagementsysytem.enums.Gender;
import com.ebuka.employeemanagementsysytem.model.dto.ResponseDto;
import com.ebuka.employeemanagementsysytem.model.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String message;
    private UsersDto usersDto;
    private ResponseDto responseDto;

    public RegisterResponse(String message, UsersDto usersDto) {
        this.message = message;
        this.usersDto = usersDto;
        this.responseDto = null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsersDto{
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private Gender gender;

        public UsersDto(Users users) {
            this.firstName = users.getFirstName();
            this.lastName = users.getLastName();
            this.email = users.getEmail();
            this.phoneNumber = users.getPhoneNumber();
            this.gender = users.getGender();
        }
    }
}


