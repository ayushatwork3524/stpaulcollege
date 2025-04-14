package com.main.stpaul.dto.request;

import lombok.Data;

@Data
public class GuardianInfoRequest {
    private String name;
    private String phone;
    private String occupation;
    private String relation;
    private double income;
}
