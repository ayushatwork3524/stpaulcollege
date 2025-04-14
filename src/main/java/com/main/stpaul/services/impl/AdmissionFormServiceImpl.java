package com.main.stpaul.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.AdmissionForm;
import com.main.stpaul.repository.AdmissionFormRepo;
import com.main.stpaul.services.serviceInterface.AdmissionFormService;


@Service
public class AdmissionFormServiceImpl implements AdmissionFormService{

    @Autowired
    private AdmissionFormRepo admissionFormRepo;

    @Override
    public AdmissionForm addAdmissionForm(AdmissionForm admissionForm) {
        String id = UUID.randomUUID().toString();
        admissionForm.setAdmissionId(id);
        return this.admissionFormRepo.save(admissionForm);
    }
    
}
