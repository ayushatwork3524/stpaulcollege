package com.main.stpaul.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuardianInfoResponse {
    private String giId;
    private String name;
    private String phone;
    private String occupation;
    private String relation;
    private double income;
}
