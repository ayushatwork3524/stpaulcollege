package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.response.ReceiptResponse;
import com.main.stpaul.entities.Receipt;

@Component
public class ReceiptMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public Receipt toReceipt(ReceiptResponse ReceiptResponse){
        return this.modelMapper.map(ReceiptResponse, Receipt.class);
    }
    public ReceiptResponse toReceiptResponse(Receipt receipt){
        return this.modelMapper.map(receipt, ReceiptResponse.class);
    }
}
