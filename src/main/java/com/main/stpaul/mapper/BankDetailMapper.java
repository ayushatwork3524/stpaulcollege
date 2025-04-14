package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.request.BankDetailRequest;
import com.main.stpaul.dto.response.BankDetailResponse;
import com.main.stpaul.entities.BankDetail;

@Component
public class BankDetailMapper {
    

    @Autowired
    private ModelMapper modelMapper;

    public BankDetail toBankDetail(BankDetailRequest bankDetail){
        return this.modelMapper.map(bankDetail, BankDetail.class);
    }

    public BankDetailResponse toBankDetailResponse(BankDetail bankDetail){
        return this.modelMapper.map(bankDetail, BankDetailResponse.class);
    }
}
