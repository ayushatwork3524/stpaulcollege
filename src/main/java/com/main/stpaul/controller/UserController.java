package com.main.stpaul.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.response.StudentAcademicsResponse;
import com.main.stpaul.entities.PaymentDetail;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.entities.User;
import com.main.stpaul.mapper.PaymentDetailMapper;
import com.main.stpaul.mapper.StudentAcademicsMapper;
import com.main.stpaul.mapper.UserMapper;
import com.main.stpaul.services.impl.CollegeFeesServiceImpl;
import com.main.stpaul.services.impl.PaymentDetailServiceImpl;
import com.main.stpaul.services.impl.ReceiptServiceImpl;
import com.main.stpaul.services.impl.StudentAcademicsServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;
import com.main.stpaul.services.impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = { "http://localhost:5173/", "http://localhost:5174/" })
public class UserController {
    
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private StudentAcademicsServiceImpl studentAcademicsServiceImpl;

    @Autowired
    private CollegeFeesServiceImpl collegeFeesServiceImpl;

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private PaymentDetailServiceImpl paymentDetailServiceImpl;

    @Autowired
    private ReceiptServiceImpl receiptServiceImpl;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentAcademicsMapper studentAcademicsMapper;

    @Autowired
    private PaymentDetailMapper paymentDetailMapper;

    @GetMapping("/getProfile")
    @Operation(summary = "Get user by JwtToken", description = "Fetches user details by JwtToken")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization")String jwt)throws Exception{
        log.info("Starting getProfile method with JWT");
        try {
            User user = this.userServiceImpl.getUserByJwt(jwt);
            DataResponse response = DataResponse.builder()
                                                .data(userMapper.toUserResponse(user))
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("get Profile successfully !")
                                                .build();
            log.info("Profile fetched successfully for user: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error to fetch profile : {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/college/fees")
    @Operation(summary = "Get all college fees", description = "Fetches all college fees details")
    public ResponseEntity<?> getAllCollegeFees(@RequestParam(required = false)String stdClass)throws Exception{
        log.info("Starting getAllCollegeFees method");
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.collegeFeesServiceImpl.getAllCollegeFees(stdClass))
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("get All Fees successfully !")
                                                .build();
            log.info("Fetch College fees Successfully ");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error to fetch College fees", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/college/fees/{stdClass}")
    @Operation(summary = "Get college fees by class", description = "Fetches college fees details for a specific class")
    public ResponseEntity<?> getAllCollegeFeesByClass(@PathVariable("stdClass")String stdClass)throws Exception{
        log.info("Starting getAllCollegeFeesByClass method with stdClass: {}", stdClass);
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.collegeFeesServiceImpl.getCollegeFeesByClass(stdClass))
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("get All Fees successfully !")
                                                .build();
            log.info("Fetched College fees Successfully by Class : {}",stdClass);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error to fetch College fees", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/college/classes")
    @Operation(summary = "Get distinct classes", description = "Fetches all distinct classes available in the college")
    public ResponseEntity<?> getDistinctClasses()throws Exception{
        log.info("Starting getDistinctClasses method");
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.collegeFeesServiceImpl.distinctClasses())
                                                .message("Get All Distinct Classes")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            log.info("Successfully retrieved distinct classes");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error retrieving distinct classes: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/students")
    @Operation(summary = "Get all students", description = "Fetches details of all students")
    public ResponseEntity<?> allStudents(@RequestParam(required = false)String query,
                                        @RequestParam(required = false)String stdClass,
                                        @RequestParam(required = false)String section,
                                        @RequestParam(required = false)String session) throws Exception{
        log.info("Starting allStudents method");
        try {
            DataResponse response = DataResponse.builder()
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("Get All Users Successfully !")
                                                .data(this.studentServiceImpl.getAllStudents(query,stdClass,section,session))
                                                .build();
            log.info("All Students Fetched Successfully ");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/students/{studentId}/academics")
    @Operation(summary = "Get academics details by student ID", description = "Fetches academic details for a specific student by their ID")
    public ResponseEntity<?> getAcademicsDetail(@PathVariable("studentId")String studentId)throws Exception{
        log.info("Starting getAcademicsDetail method with studentId: {}", studentId);
        try {
            List<StudentAcademics> students =this.studentAcademicsServiceImpl.getAcademicsByStudent(studentId);
            List<StudentAcademicsResponse> studentAcademicsResponses = new ArrayList<>();
            for(StudentAcademics st:students){
                StudentAcademicsResponse studentAcademicsResponse = studentAcademicsMapper.toStudentAcademicsResponse(st);  
                PaymentDetail paymentDetail = this.paymentDetailServiceImpl.getPaymentDetailByStudent(st.getStudentAcademicsId());
                if(paymentDetail == null){
                    studentAcademicsResponse.setPaymentDetail(null);
                } else {
                    studentAcademicsResponse.setPaymentDetail(paymentDetailMapper.toPaymentDetailResponse(paymentDetail));
                    studentAcademicsResponse.getPaymentDetail().setReceipt(this.receiptServiceImpl.getReceiptByPaymentDetail(st.getPaymentDetail().getPaymentDetailId()));
                }
                studentAcademicsResponses.add(studentAcademicsResponse);
            }
            DataResponse response = DataResponse.builder()
                                                .data(studentAcademicsResponses)
                                                .message("Get All Academics Successfully !")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            log.info("Successfully retrieved academics details for studentId: {}", studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error retrieving academics details for studentId: {}: {}", studentId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


    @GetMapping("/students/fail")
    public ResponseEntity<?> getAllFailStudents(@RequestParam(required = false)String query,
                                                @RequestParam(required = false)String stdClass,
                                                @RequestParam(required = false)String section,
                                                @RequestParam(required = false)String session)throws Exception{
        log.info("Starting getAllFailStudents method");
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.studentServiceImpl.getFailStudents(query,stdClass,section,session))
                                                .message("Get All Fail Students Successfully !")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            log.info("All Fail Students Fetched Successfully ");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Fail Students {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
