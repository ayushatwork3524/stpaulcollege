package com.main.stpaul.services.serviceInterface;

import java.util.List;

import com.main.stpaul.entities.StudentAcademics;

public interface StudentAcademicsService {
    StudentAcademics addStudentAcademics(StudentAcademics studentAcademics);
    List<StudentAcademics> getAcademicsByStudent(String studentId);
    StudentAcademics getAcademicsById(String id);
    StudentAcademics getOngoingAcademicsByStudent(String studentId);
    public StudentAcademics getPendingAcademicsByStudent(String studentId);
    void updateStudentAcademics(StudentAcademics studentAcademics) throws Exception;
}
