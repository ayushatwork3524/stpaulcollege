package com.main.stpaul.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmissionFormRequest {
    private LocalDate admissionDate;
    private String session;
    private String formNo;
    private String stdClass;
}
