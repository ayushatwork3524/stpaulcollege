package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.AdmissionForm;

public interface AdmissionFormRepo extends JpaRepository<AdmissionForm,String>{
    
}
