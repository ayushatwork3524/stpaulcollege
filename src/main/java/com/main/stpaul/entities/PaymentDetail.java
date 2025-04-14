package com.main.stpaul.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class PaymentDetail {

    @Id
    private String paymentDetailId;
    
    private double totalFees;
    private double balanceAmount;
    private double paidAmount;
    private String paymentType;
    private double installmentAmount;
    private int installmentGap;
    private int installments;
    private LocalDate dueDate;

    private boolean isDelete = false;
    private boolean isActive = true;
    
    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "student_academics_id", unique = true, nullable = false)
    private StudentAcademics studentAcademics;

    @OneToMany(mappedBy = "paymentDetail")
    private List<Receipt> receipts;
}
