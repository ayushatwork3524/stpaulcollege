package com.main.stpaul.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.constants.Result;
import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.helper.StudentHelper;
import com.main.stpaul.services.impl.StudentAcademicsServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private StudentAcademicsServiceImpl studentAcademicsServiceImpl;

    
    @PostMapping("/student/promote")
    public ResponseEntity<?> promoteStudent(@RequestBody List<String> studentIds)throws Exception{
        log.info("Starting promoteStudent method with studentIds: {}", studentIds);
        try {
            for(String id:studentIds){
                log.info("Processing student with ID: {}", id);
                Student student = this.studentServiceImpl.getStudentById(id);
                String stdClass = student.getStdClass();
                if(student==null){
                    log.error("Student not found with ID: {}", id);
                    throw new EntityNotFoundException("Student not found !");
                }
                
                if (stdClass.equals("12")) {
                    student.setStatus(Status.Completed); 
                }
                else{
                    student.setStatus(Status.Ongoing);
                    student.setStdClass(String.valueOf(Integer.parseInt(student.getStdClass())+1));
                }
                student.setSession(StudentHelper.sessionIncrementer(student.getSession()));
                this.studentServiceImpl.updateStudent(student);

                log.info("Promoted student with ID: {}", id);

                StudentAcademics academics = this.studentAcademicsServiceImpl.getOngoingAcademicsByStudent(student.getStudentId());
                if(academics == null){
                    log.warn("No ongoing academics found for student ID: {}", id);
                }else{
                    academics.setStatus(Status.Completed);
                    academics.setUpdatedDate(LocalDateTime.now());
                    academics.setResult(Result.PASS);
                    academics.setPromotionDate(LocalDate.now());
                    this.studentAcademicsServiceImpl.updateStudentAcademics(academics);
                }

                log.info("Updated student academics for student ID: {}", id);

                if(stdClass.equals("12")){
                    log.info("Student with ID: {} has completed their studies.", id);
                }
                else{
                    StudentAcademics newAcademics = new StudentAcademics();
                    newAcademics.setStdClass(student.getStdClass());
                    newAcademics.setSession(student.getSession());
                    newAcademics.setStudent(student);
                    this.studentAcademicsServiceImpl.addStudentAcademics(newAcademics);
                }

                log.info("Added new student academics for student ID: {}", id);
            }
            log.info("Successfully promoted students: {}", studentIds.toString());
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200, "Student promoted successfully !");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            log.error("Error promoting students: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/student/assignRollNoAndSection")
    
    public ResponseEntity<?> assignRollNoAndSection(@RequestBody List<String> studentIds)throws Exception{
        log.info("Starting assignRollNoAndSection method with studentIds: {}", studentIds);
        try {
            for(String id:studentIds){
                log.info("Processing student with ID: {}", id);
                Student student = this.studentServiceImpl.getStudentById(id);
                if(student==null){
                    log.error("Student not found with ID: {}", id);
                    throw new EntityNotFoundException("Student not found !");
                }
                student.setStatus(Status.Ongoing);
                student.setStdClass(String.valueOf(Integer.parseInt(student.getStdClass())+1));
                student.setSession(StudentHelper.sessionIncrementer(student.getSession()));
                this.studentServiceImpl.updateStudent(student);

                log.info("Assigned roll number and section for student with ID: {}", id);
            }
            DataResponse response = DataResponse.builder()
                                    .data(studentIds)
                                    .message("Roll number and section assigned successfully !")
                                    .status(HttpStatus.OK)
                                    .statusCode(200)
                                    .build();
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            log.error("Error assigning roll number and section: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
