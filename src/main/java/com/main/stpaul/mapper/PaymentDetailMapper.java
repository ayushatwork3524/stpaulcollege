package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.request.PaymentDetailRequest;
import com.main.stpaul.dto.response.PaymentDetailResponse;
import com.main.stpaul.entities.PaymentDetail;

@Component
public class PaymentDetailMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public PaymentDetail toPaymentDetail(PaymentDetailRequest paymentDetailRequest){
        return this.modelMapper.map(paymentDetailRequest, PaymentDetail.class);
    }

    public PaymentDetailResponse toPaymentDetailResponse(PaymentDetail paymentDetailRequest){
        return this.modelMapper.map(paymentDetailRequest, PaymentDetailResponse.class);
    }
}
