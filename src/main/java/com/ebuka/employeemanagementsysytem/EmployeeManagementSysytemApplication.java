package com.ebuka.employeemanagementsysytem;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableRabbit
@EnableJpaRepositories(basePackages = "com.ebuka.employeemanagementsysytem.repository")
@EntityScan(basePackages = "com.ebuka.employeemanagementsysytem.model.entity")
public class EmployeeManagementSysytemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementSysytemApplication.class, args);
    }

}
