package com.main.stpaul.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeFeesResponse {
    private long id;
    private String stdClass;
    private double totalFees;
    private int installmentGap;
    private double installmentsAmount; 
    private long installments;
}