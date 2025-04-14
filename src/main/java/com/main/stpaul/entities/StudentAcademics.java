package com.main.stpaul.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.stpaul.constants.Result;
import com.main.stpaul.constants.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class StudentAcademics {

    @Id
    private String studentAcademicsId;

    private String collegeName="S.T. Paul College";
    private String rollNo;
    private String examination;
    private String examMonth;
    private int marksObtained;
    private String stdClass;
    private String session;
    private String section;

    @Enumerated(EnumType.STRING)
    private Result result=Result.ON_GOING;
    private boolean isAlumni;
    private LocalDate promotionDate;

    @Enumerated(EnumType.STRING)
    private Status status=Status.Pending;

    private boolean isDelete = false;
    private boolean isActive = true;
    
    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    @OneToOne(mappedBy = "studentAcademics")
    private PaymentDetail paymentDetail;

    @OneToOne(mappedBy = "academics",cascade = CascadeType.ALL, orphanRemoval = true)
    private Stream stream;

    @OneToOne(mappedBy = "academics",cascade = CascadeType.ALL, orphanRemoval = true)
    private BiofocalSubject biofocalSubject;
}