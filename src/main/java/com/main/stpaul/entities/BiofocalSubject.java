package com.main.stpaul.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class BiofocalSubject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String subStream;
    private String subject;
    private String medium="English";

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "student_academics_id")
    private StudentAcademics academics;
}
