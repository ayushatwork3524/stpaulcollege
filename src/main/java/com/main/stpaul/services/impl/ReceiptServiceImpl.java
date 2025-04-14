package com.main.stpaul.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.Receipt;
import com.main.stpaul.repository.ReceiptRepo;
import com.main.stpaul.services.serviceInterface.ReceiptService;

@Service
public class ReceiptServiceImpl implements ReceiptService{

    @Autowired
    private ReceiptRepo receiptRepo;

    @Override
    public Receipt addReceipt(Receipt receipt) {
        return this.receiptRepo.save(receipt);
    }

    @Override
    public Receipt getReceiptById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReceiptById'");
    }

    public Receipt findByid(Long id){
        return this.receiptRepo.findById(id).get();
    }

    @Override
    public List<Receipt> getReceiptByPaymentDetail(String id) {
        return this.receiptRepo.findByPaymentDetail(id);
    }
    
}
