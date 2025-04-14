package com.main.stpaul.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetailResponse {
    private String bankDetailId;
    private String bankName;
    private String branchName;
    private String accountNo;
    private String ifscCode;
}
