package com.ebuka.employeemanagementsysytem.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private String status;
    private String message;
    private ResponseData data;
}
