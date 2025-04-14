package com.main.stpaul.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.request.GuardianInfoRequest;
import com.main.stpaul.entities.GuardianInfo;
import com.main.stpaul.repository.GuardianInfoRepo;
import com.main.stpaul.services.serviceInterface.GuardianInfoService;


@Service
public class GuardianInfoServiceImpl implements GuardianInfoService{

    @Autowired
    private GuardianInfoRepo guardianInfoRepo;

    @Override
    public GuardianInfo addGuardianInfo(GuardianInfo guardianInfo) {
        String id = UUID.randomUUID().toString();
        guardianInfo.setGiId(id);
        return this.guardianInfoRepo.save(guardianInfo);
    }

    @Override
    public GuardianInfo getGuardianInfoById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGuardianInfoById'");
    }

    @Override
    public GuardianInfo getGuardianInfoByStudent(String studentId) {
        return this.guardianInfoRepo.findByStudentId(studentId).orElse(null);
    }

    @Override
    public boolean updateGuardianInfo(GuardianInfoRequest guardianInfoRequest,String id) {
        this.guardianInfoRepo.updateGuardianInfo(guardianInfoRequest.getName(),guardianInfoRequest.getPhone(),guardianInfoRequest.getOccupation(),guardianInfoRequest.getRelation(),guardianInfoRequest.getIncome(),id);
        return true;
    }

    @Override
    public boolean deleteGuardianInfo(String id) {
       
        this.guardianInfoRepo.deleteGuardianInfo(id);
        return true;
        
    }
    
}
