package com.main.stpaul.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Documents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long documentId;
    private String documentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] document;
    
    private boolean isDelete = false;
    private boolean isActive = true;

    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
