package com.main.stpaul.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.constants.Status;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.repository.StudentAcademicsRepo;
import com.main.stpaul.services.serviceInterface.StudentAcademicsService;

@Service
public class StudentAcademicsServiceImpl implements StudentAcademicsService{

    @Autowired
    private StudentAcademicsRepo studentAcademicsRepo;

    @Override
    public StudentAcademics addStudentAcademics(StudentAcademics studentAcademics) {
        String id = UUID.randomUUID().toString();
        studentAcademics.setStudentAcademicsId(id);
        return this.studentAcademicsRepo.save(studentAcademics);
    }

    @Override
    public List<StudentAcademics> getAcademicsByStudent(String studentId) {
        return this.studentAcademicsRepo.findByStudentId(studentId);
    }

    @Override
    public StudentAcademics getAcademicsById(String id) {
        return this.studentAcademicsRepo.findAcademicsById(id).orElse(null);
    }

    @Override
    public StudentAcademics getOngoingAcademicsByStudent(String studentId) {
        return this.studentAcademicsRepo.findOngoingAcademicsByStudent(studentId, Status.Ongoing).orElse(null);
    }

    @Override
    public StudentAcademics getPendingAcademicsByStudent(String studentId) {
        return this.studentAcademicsRepo.findPendingAcademicsByStudent(studentId, Status.Pending).orElse(null);
    }

    @Override
    public void updateStudentAcademics(StudentAcademics studentAcademics){
        this.studentAcademicsRepo.save(studentAcademics);
        return ;
    }
    
}
