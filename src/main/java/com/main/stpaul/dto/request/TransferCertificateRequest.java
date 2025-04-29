package com.main.stpaul.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferCertificateRequest {

    private String studentId;

    private String tcNo;

    private String admissionNo;

    private String duePaid;

    private String totalDays;

    private String totalPresentDays;

    private String dateOfApplication;

    private String dateOfIssue;

    private String reason;

    private String remark;

    private String generalConduct;

    private String religion;

    private String category;

    private String place;

    private String fee;

    private String ncc;

    private String games;

    private String conduct;

    private String tongue;

}
