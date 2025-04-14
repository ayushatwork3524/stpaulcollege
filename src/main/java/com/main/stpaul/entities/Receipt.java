package com.main.stpaul.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.stpaul.constants.PaymentMode;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receipt_seq")
    @SequenceGenerator(name = "receipt_seq", sequenceName = "receipt_sequence", allocationSize = 1)
    private Long receiptNo;
    private double amountPaid;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;
    private LocalDateTime paymentDate;
    private String transactionId;

    private boolean isDelete = false;
    private boolean isActive = true;

    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "payment_detail_id")
    private PaymentDetail paymentDetail;
}
