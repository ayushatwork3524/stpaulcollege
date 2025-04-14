package com.main.stpaul.dto.request;

import lombok.Data;

@Data
public class LastSchoolRequest {
    private String collegeName;
    private String lastStudentId;
    private String rollNo;
    private String uid;
    private String examination;
    private String examMonth;
    private int marksObtained;
    private String result;
}
