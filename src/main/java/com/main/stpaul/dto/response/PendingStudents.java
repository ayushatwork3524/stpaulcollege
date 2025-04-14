package com.main.stpaul.dto.response;

import java.time.LocalDate;

import com.main.stpaul.constants.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingStudents {
    
    public String studentId;
    public String firstName;
    public String fatherName;
    public String surName;
    public String email;
    public String contact;
    private String gender;
    public LocalDate dob;
    public LocalDate dateOfAdmission;
    public String academicId;
    public String session;
    public String stdClass;
    public Status status;
    public double totalFees;
}
