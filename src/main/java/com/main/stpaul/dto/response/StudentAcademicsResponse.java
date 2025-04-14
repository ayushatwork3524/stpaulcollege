package com.main.stpaul.dto.response;

import java.time.LocalDate;

import com.main.stpaul.constants.Result;
import com.main.stpaul.constants.Status;
import com.main.stpaul.entities.BiofocalSubject;
import com.main.stpaul.entities.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAcademicsResponse {
    private String studentAcademicsId;

    private String collegeName;
    private String rollNo;
    private String examination;
    private String examMonth;
    private int marksObtained;
    private String stdClass;
    private Result result;
    private boolean isAlumni;
    private LocalDate promotionDate;
    private Status status;
    private String section;

    private PaymentDetailResponse paymentDetail;
    private Stream stream;
    private BiofocalSubject biofocalSubject;
}
