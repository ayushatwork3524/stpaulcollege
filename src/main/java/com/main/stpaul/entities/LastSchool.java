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
public class LastSchool {

    @Id
    private String lsId;

    private String collegeName;
    private String lastStudentId;
    private String rollNo;
    private String uid;
    private String examination;
    private String examMonth;
    private double marksObtained;
    private String result;

    private boolean isDelete = false;
    private boolean isActive = true;
    
    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @JsonIgnore
    @JoinColumn(name = "student_id")
    @OneToOne
    private Student student;

}
