package com.main.stpaul.dto.request;

import lombok.Data;

@Data
public class CollegeFeesRequest {
    private String stdClass;
    private double totalFees;
    private int installmentGap;
    private double installmentsAmount;
    private long installments;
}
