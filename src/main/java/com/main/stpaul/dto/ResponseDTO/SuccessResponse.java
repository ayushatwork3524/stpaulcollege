package com.main.stpaul.dto.ResponseDTO;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {
    private HttpStatus status;
    private int statusCode;
    private String message;
}
