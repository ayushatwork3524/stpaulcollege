package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.main.stpaul.dto.response.LeavingCertificateResponse;
import com.main.stpaul.entities.LeavingCertificate;

@Component
public class LeavingCertificateMapper {
    
     @Autowired
    private ModelMapper modelMapper;


    public LeavingCertificateResponse toLeavingCertificateResponse(LeavingCertificate leavingCertificate){
        return this.modelMapper.map(leavingCertificate, LeavingCertificateResponse.class);
    }
}
