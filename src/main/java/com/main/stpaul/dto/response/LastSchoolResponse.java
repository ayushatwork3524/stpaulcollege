package com.main.stpaul.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastSchoolResponse {
    private String lsId;
    private String collegeName;
    private String lastStudentId;
    private String rollNo;
    private String uid;
    private String examination;
    private String examMonth;
    private int marksObtained;
    private String result;
}
