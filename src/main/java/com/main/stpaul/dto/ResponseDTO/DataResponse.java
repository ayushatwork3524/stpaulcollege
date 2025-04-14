package com.main.stpaul.dto.ResponseDTO;

import org.springframework.http.HttpStatus;

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
public class DataResponse {
    private String message;
    private HttpStatus status;
    private int statusCode;
    private Object data;
}