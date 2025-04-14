package com.main.stpaul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.Exceptions.DuplicateEntityException;
import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.dto.request.CollegeFeesRequest;
import com.main.stpaul.entities.CollegeFees;
import com.main.stpaul.mapper.CollegeFeesMappler;
import com.main.stpaul.services.impl.CollegeFeesServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {

    
    @Autowired
    private CollegeFeesServiceImpl collegeFeesServiceImpl;

    @Autowired
    private CollegeFeesMappler collegeFeesMappler;

    
    @PostMapping("/college/fees")    
    public ResponseEntity<?> addCollegeFees(@RequestBody CollegeFeesRequest request)throws Exception{
        try {
            CollegeFees cf = this.collegeFeesServiceImpl.getCollegeFeesByClass(request.getStdClass());
            if(cf!=null){
                throw new DuplicateEntityException("Class alredy found !");
            }
            this.collegeFeesServiceImpl.addCollegeFees(this.collegeFeesMappler.toCollegeFees(request));
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Fees Added Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/college/fees/{id}")    
    public ResponseEntity<?> addCollegeFeesById(@PathVariable("id")long id)throws Exception{
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.collegeFeesServiceImpl.getCollegeFees(id))
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("get All Fees successfully !")
                                                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/college/fees/{id}")    
    public ResponseEntity<?> updateCollegeFees(@PathVariable("id")long id ,@RequestBody CollegeFeesRequest request)throws Exception{
        try {
            log.info("College fees Updating ....");
            this.collegeFeesServiceImpl.updateCollegeFees(request, id);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"College fees Updated Successfully !");
            log.info("College Fees Updated Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error while updating the college fees : {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/college/fees/{id}")
    public ResponseEntity<?> deleteCollegeFees(@PathVariable("id")long id)throws Exception{
        try {
            this.collegeFeesServiceImpl.deleteCollegeFees(id);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"College fees deleted Successfully !");
            log.info("College Fees Deleted Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
