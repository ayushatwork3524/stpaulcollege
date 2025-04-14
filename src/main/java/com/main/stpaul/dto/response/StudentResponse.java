package com.main.stpaul.dto.response;

import java.time.LocalDate;

import com.main.stpaul.constants.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private String studentId;

    private String firstName;
    private String fatherName;
    private String motherName;
    private String surname;
    private String email;
    private String phoneNo;
    private LocalDate dateOfBirth;
    private String gender;
    private byte[] image;
    
    private String localAddress;
    private String permanentAddress;

    private String adharNo;
    private String bloodGroup;
    private String caste;
    private String category;
    private String scholarshipCategory;

    private LocalDate admissionDate;
    private String session;
    private String stdClass;
    private Status status;
}
