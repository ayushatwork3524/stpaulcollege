package com.main.stpaul.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BioFocalSubjectRequest {
    private String subStream;
    private String subject;
    private String medium;
}
