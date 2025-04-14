package com.main.stpaul.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.request.BankDetailRequest;
import com.main.stpaul.entities.BankDetail;
import com.main.stpaul.repository.BankDetailRepo;
import com.main.stpaul.services.serviceInterface.BankDetailService;

@Service
public class BankDetailServiceImpl implements BankDetailService{

    @Autowired
    private BankDetailRepo bankDetailRepo;

    @Override
    public BankDetail addBankDetail(BankDetail bankDetail) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
       // String bankId = id.replaceAll("-", "");
        bankDetail.setBankDetailId(id);
        return this.bankDetailRepo.save(bankDetail);
    }

    @Override
    public BankDetail getBankDetailById(String bdId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBankDetailById'");
    }

    @Override
    public BankDetail getBankDetailByStudent(String studentId) {
        return this.bankDetailRepo.findByStudentId(studentId).orElse(null);
    }

    @Override
    public void updateBankDetail(BankDetailRequest bankDetail,String id) {
        this.bankDetailRepo.updateBankDetail(bankDetail.getBankName(),bankDetail.getBranchName(),bankDetail.getAccountNo(),bankDetail.getIfscCode(),id);
    }

    @Override
    public void deleteBankDetail(String id) {
        this.bankDetailRepo.deleteBankDetail(id);
    }
    
}
