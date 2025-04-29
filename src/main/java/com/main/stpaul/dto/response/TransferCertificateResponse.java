package com.main.stpaul.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferCertificateResponse {

    private long srNo;

    private int copyNumber;

    private String issueAt;

    private byte[] data;
}
