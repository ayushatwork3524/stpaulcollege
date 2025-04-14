package com.main.stpaul.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.Subject;
import com.main.stpaul.repository.SubjectRepo;
import com.main.stpaul.services.serviceInterface.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService{

    @Autowired
    private SubjectRepo subjectRepo;

    @Override
    public Subject addSubject(Subject subject) {
        return this.subjectRepo.save(subject);
    }
    
}
