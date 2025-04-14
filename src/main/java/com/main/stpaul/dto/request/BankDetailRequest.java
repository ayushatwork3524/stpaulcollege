package com.main.stpaul.dto.request;

import lombok.Data;

@Data
public class BankDetailRequest {
    private String bankName;
    private String branchName;
    private String accountNo;
    private String ifscCode;
}
