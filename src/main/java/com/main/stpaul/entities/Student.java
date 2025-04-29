package com.main.stpaul.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.stpaul.constants.Status;

@Data
@Entity
@Table(name = "Student")
public class Student {

    @Id
    private String studentId;

    private String firstName;
    private String fatherName;
    private String motherName;
    private String surname;
    private String email;
    private String phoneNo;
    private LocalDate dateOfBirth;
    private String gender;

    private String adharNo;
    private String bloodGroup;
    private String caste;
    private String category;
    private String scholarshipCategory;

    private String localAddress;
    private String permanentAddress;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    private LocalDate admissionDate;
    private String session;
    private String stdClass;
    private String section;
    private String rollNo;
    private long uDiseNo;

    @Enumerated(EnumType.STRING)
    private Status status = Status.Pending;

    private boolean isDelete = false;
    private boolean isActive = true;

    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentAcademics> studentAcademics;

    @OneToOne(mappedBy = "student")
    private TransferCertificate transferCertificate;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Documents> documents;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private GuardianInfo guardianInfo;

    @OneToOne(mappedBy = "student")
    private BankDetail bankDetail;

    @OneToOne(mappedBy = "student")
    private LastSchool lastSchool;

    @JsonIgnore
    @OneToOne(mappedBy = "student")
    private AdmissionForm admissionForm;

    public String getFullName() {
        return this.firstName + " " + this.fatherName + " " + this.surname;
    }
}
