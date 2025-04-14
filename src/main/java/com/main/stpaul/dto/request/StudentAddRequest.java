package com.main.stpaul.dto.request;

import lombok.Data;

@Data
public class StudentAddRequest {
    AdmissionFormRequest admissionForm;
    StudentRequest student;
    LastSchoolRequest lastSchool;
    // BankDetailRequest bankDetail;
    GuardianInfoRequest guardianInfo;
    StreamRequest subject;
    BioFocalSubjectRequest bioFocalSubject;
}
