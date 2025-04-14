package com.main.stpaul.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.request.CollegeFeesRequest;
import com.main.stpaul.entities.CollegeFees;
import com.main.stpaul.repository.CollegeFeesRepo;
import com.main.stpaul.services.serviceInterface.CollegeFeesService;

@Service
public class CollegeFeesServiceImpl implements CollegeFeesService{

    @Autowired
    private CollegeFeesRepo collegeFeesRepo;

    @Override
    public CollegeFees addCollegeFees(CollegeFees collegeFees) {
        return this.collegeFeesRepo.save(collegeFees);
    }


    @Override
    public List<CollegeFees> getAllCollegeFees(String stdClass) {
        return this.collegeFeesRepo.getAllCollegeFees(stdClass);
    }

    @Override
    public CollegeFees getCollegeFees(long id) {
        return this.collegeFeesRepo.findCollegeFeesById(id).orElse(null);
    }

  
    @Override
    public void deleteCollegeFees(long id) {
        this.collegeFeesRepo.deleteCollegeFees(id);
    }


    @Override
    public void updateCollegeFees(CollegeFeesRequest collegeFees, long id) {
        this.collegeFeesRepo.updateCollegeFees(collegeFees.getStdClass(),collegeFees.getTotalFees(),collegeFees.getInstallmentGap(),collegeFees.getInstallmentsAmount(),collegeFees.getInstallments(),id);
    }


    @Override
    public double getTotalFeesByClass(String stdClass) {
        return this.collegeFeesRepo.getFeesByClass(stdClass).orElse(0.0);
    }


    @Override
    public CollegeFees getCollegeFeesByClass(String Class) {
        return this.collegeFeesRepo.getCollegefeesFeesByClass(Class).orElse(null);
    }


    @Override
    public List<String> distinctClasses() {
        return this.collegeFeesRepo.distinctClasses();
    }
    
}
