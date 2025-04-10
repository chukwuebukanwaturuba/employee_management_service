package com.ebuka.employeemanagementsysytem.repository;

import com.ebuka.employeemanagementsysytem.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUsersByEmailOrPhoneNumber(String email, String phoneNumber);
    Users findByEmailOrPhoneNumber(String email, String phoneNumber);
    boolean existsByEmployeeId(String employeeId);

    Optional<Users> findByEmail(String email);

}
