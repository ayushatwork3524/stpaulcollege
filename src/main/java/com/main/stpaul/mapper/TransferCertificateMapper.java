package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.response.TransferCertificateResponse;
import com.main.stpaul.entities.TransferCertificate;

@Component
public class TransferCertificateMapper {

    @Autowired
    private ModelMapper modelMapper;

    public TransferCertificateResponse toTransferCertificateResponse(TransferCertificate transferCertificate) {
        return this.modelMapper.map(transferCertificate, TransferCertificateResponse.class);
    }
}
