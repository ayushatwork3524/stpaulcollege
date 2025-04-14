package com.main.stpaul.mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.request.StudentRequest;
import com.main.stpaul.dto.response.StudentDetailResponse;
import com.main.stpaul.dto.response.StudentResponse;
import com.main.stpaul.entities.Student;

@Component
public class StudentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Student toStudent(StudentRequest studentRequest){
        return this.modelMapper.map(studentRequest, Student.class);
    }

    public StudentDetailResponse toStudentDetailResponse(StudentResponse studentResponse){
        return this.modelMapper.map(studentResponse, StudentDetailResponse.class);
    }

    public StudentDetailResponse toStudentDetailResponse(Student studentResponse){
        return this.modelMapper.map(studentResponse, StudentDetailResponse.class);
    }

    public Student toStudent(StudentDetailResponse response){
        return this.modelMapper.map(response, Student.class);
    }
}
