package com.main.stpaul.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.response.StudentAcademicsResponse;
import com.main.stpaul.entities.StudentAcademics;

@Component
public class StudentAcademicsMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public StudentAcademics toStudentAcademics(StudentAcademicsResponse studentAcademicsResponse){
        return this.modelMapper.map(studentAcademicsResponse, StudentAcademics.class);
    }

    public StudentAcademicsResponse toStudentAcademicsResponse(StudentAcademics studentAcademics){
        return this.modelMapper.map(studentAcademics, StudentAcademicsResponse.class);
    }

    public List<StudentAcademicsResponse> toStudentAcademicsResponseList(List<StudentAcademics> studentAcademicsList){
        return studentAcademicsList.stream().map(studentAcademics -> this.toStudentAcademicsResponse(studentAcademics)).collect(Collectors.toList());
    }
}
