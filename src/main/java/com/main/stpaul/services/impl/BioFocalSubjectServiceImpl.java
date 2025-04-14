package com.main.stpaul.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.BiofocalSubject;
import com.main.stpaul.repository.BioFocalSubjectRepo;
import com.main.stpaul.services.serviceInterface.BioFocalSubjectService;

@Service
public class BioFocalSubjectServiceImpl implements BioFocalSubjectService{

    @Autowired
    private BioFocalSubjectRepo bioFocalSubjectRepo;

    @Override
    public BiofocalSubject addBiofocalSubject(BiofocalSubject biofocalSubject) {
        return this.bioFocalSubjectRepo.save(biofocalSubject);
    }

    @Override
    public BiofocalSubject getBiofocalSubjectById(Long id) {
        return this.bioFocalSubjectRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteBiofocalSubjectById(Long id) {
        this.bioFocalSubjectRepo.deleteById(id);
    }
    
}
