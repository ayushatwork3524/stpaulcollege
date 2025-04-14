package com.main.stpaul.services.serviceInterface;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.main.stpaul.dto.response.PendingStudents;
import com.main.stpaul.entities.Student;

public interface StudentService {

    Student addStudent(Student student);
    Student findById(String id);
    Student getStudentById(String id);
    Student updateStudent(Student student);
    
    void deleteStudent(String id);
    
    List<Student> getAllStudents(String query,String stdClass,String section,String session);
    List<PendingStudents> getPendingStudents(String query,String stdClass,String section,String session);
    List<Student> getFailStudents(String query,String stdClass,String section,String session);

    void saveStudentFromCSV(MultipartFile file) throws Exception;
    void saveStudentFromExcel(MultipartFile file) throws Exception;
    ByteArrayInputStream loadStudentDataToCSV(String query,String stdClass,String section,String session);
    
}