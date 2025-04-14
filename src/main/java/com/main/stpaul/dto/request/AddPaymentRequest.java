package com.main.stpaul.dto.request;

import com.main.stpaul.constants.PaymentMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPaymentRequest {
    private PaymentMode paymentMode;
    private double amountPaid;
}
