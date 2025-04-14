package com.main.stpaul.services.impl;

import java.util.UUID;
                   
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.request.LastSchoolRequest;
import com.main.stpaul.entities.LastSchool;
import com.main.stpaul.repository.LastSchoolRepo;
import com.main.stpaul.services.serviceInterface.LastSchoolService;


@Service
public class LastSchoolServiceImpl implements LastSchoolService{

    @Autowired
    private LastSchoolRepo lastSchoolRepo;

    @Override
    public LastSchool addLastSchool(LastSchool lastSchool) {
        String id = UUID.randomUUID().toString();
        lastSchool.setLsId(id);
        return this.lastSchoolRepo.save(lastSchool);
    }

    @Override
    public LastSchool getLastSchoolById(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'getLastSchoolById'");
    }

    @Override
    public LastSchool getLastSchoolByStudent(String studentId) {
        return this.lastSchoolRepo.findByStudent(studentId).orElse(null);
    }

    @Override
    public void updateLastSchool(LastSchoolRequest lastSchool,String id) {
        this.lastSchoolRepo.updateLastSchool(lastSchool.getCollegeName(),lastSchool.getLastStudentId(),lastSchool.getRollNo(),
                                            lastSchool.getUid(),lastSchool.getExamination(),lastSchool.getExamMonth(),lastSchool.getMarksObtained(),lastSchool.getResult(),id);
    }

    @Override
    public void deleteLastSchool(String lsId) {
        this.lastSchoolRepo.deleteLastSchool(lsId);   
    }
    
}
