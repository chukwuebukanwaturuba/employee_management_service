package com.ebuka.employeemanagementsysytem.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String message;
    private String time;
    private T data;
    private boolean hasError;
    public ApiResponse(T data) {
        this.message = "success";
        this.time = saveDate(LocalDateTime.now());
        this.data = data;
    }


    public ApiResponse(String message) {
        this.message = message;
    }
    public ApiResponse(T data, String message){
        this.data = data;
        this.message = message;
    }
    private static String saveDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm:ss a");
        return date.format(formatter);
    }
}
