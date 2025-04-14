package com.main.stpaul.dto.request;

import com.main.stpaul.constants.Result;
import com.main.stpaul.constants.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor  // This annotation generates a constructor with all arguments for the class.
@NoArgsConstructor // This annotation generates a no-argument constructor for the class.
public class UpdateAcademicsRequest {
    private String collegeName;
    private String stdClass;
    private String examination;
    private String examMonth;
    private String rollNo;
    private Result result;
    private int marksObtained;
    private Status status;
    private String section;
    private boolean alumni;
}
