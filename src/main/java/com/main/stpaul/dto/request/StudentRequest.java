package com.main.stpaul.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class StudentRequest {
    private String firstName;
    private String fatherName;
    private String motherName;
    private String surname;
    private String email;
    private String phoneNo;
    private LocalDate dateOfBirth;
    private String gender;
    private String localAddress;
    private String permanentAddress;

    private String adharNo;
    private String bloodGroup;
    private String caste;
    private String category;
    private String scholarshipCategory;
}
