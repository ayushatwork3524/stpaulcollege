package com.main.stpaul.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.main.stpaul.constants.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetailResponse {
    private String studentId;

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
    private byte[] image;

    private String adharNo;
    private String bloodGroup;
    private String caste;
    private String category;
    private String scholarshipCategory;

    private LocalDate admissionDate;
    private String session;
    private String section;
    private String stdClass;
    private Status status;

    private List<StudentAcademicsResponse> studentAcademics;

    private List<DocumentReponse> documents;
    
    private GuardianInfoResponse guardianInfo;

    private BankDetailResponse bankDetail;

    private LastSchoolResponse lastSchool;

    private List<LeavingCertificateResponse> leavingCertificates;
}
