package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.Subject;

public interface SubjectRepo extends JpaRepository<Subject,Long>{
    
}
