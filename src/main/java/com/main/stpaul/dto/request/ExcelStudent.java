package com.main.stpaul.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelStudent {
    private String firstName;
    private String fatherName;
    private String motherName;
    private String surname;
    private String email;
    private String phoneNo;
    private LocalDate dateOfBirth;
    private String gender;

    private String adharNo;
    private String bloodGroup;
    private String caste;
    private String category;
    private String scholarshipCategory;

    private GuardianInfoRequest guardianInfo;
    private BankDetailRequest bankDetail;
}
