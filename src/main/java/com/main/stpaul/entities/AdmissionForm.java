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
public class AdmissionForm {

    @Id
    private String admissionId;
    private String admissionDate;
    private String session;
    private String formNo;
    private String stdClass;
    
    private boolean isActive;
    private boolean isDelete;
    
    private LocalDateTime addedDate=LocalDateTime.now();
    private LocalDateTime updateDate=LocalDateTime.now();
    private LocalDateTime deleteDate;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
