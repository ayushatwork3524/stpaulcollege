package com.main.stpaul.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stream;
    private  String subStream;
    private String medium;

    @OneToMany(mappedBy = "stream",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subject> subjects;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "student_academics_id")
    private StudentAcademics academics;
}
