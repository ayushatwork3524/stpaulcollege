package com.main.stpaul.dto.ResponseDTO;

import org.springframework.http.HttpStatus;

import com.main.stpaul.dto.response.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoginResponse {
    private String message;
    private HttpStatus httpStatus;
    private int statusCode;
    private String token;
    private UserResponse user;
}
