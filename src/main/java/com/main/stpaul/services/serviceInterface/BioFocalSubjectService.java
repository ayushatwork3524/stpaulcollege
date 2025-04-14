package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.entities.BiofocalSubject;

public interface BioFocalSubjectService {
    BiofocalSubject addBiofocalSubject(BiofocalSubject biofocalSubject);
    BiofocalSubject getBiofocalSubjectById(Long id);
    void deleteBiofocalSubjectById(Long id);
}
