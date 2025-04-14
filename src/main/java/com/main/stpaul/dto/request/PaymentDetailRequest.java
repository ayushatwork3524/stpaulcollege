package com.main.stpaul.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PaymentDetailRequest {
    private double totalFees;
    private double balanceAmount;
    private double paidAmount;
    private String paymentType;
    private double installmentAmount;
    private int installmentGap;
    private int installments;
    private LocalDate dueDate;
}




