package com.main.stpaul.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class BankDetail {

    @Id
    private String bankDetailId;

    private String bankName;
    private String branchName;
    private String accountNo;
    private String ifscCode;
    
    private boolean isDelete = false;
    private boolean isActive = true;

    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
