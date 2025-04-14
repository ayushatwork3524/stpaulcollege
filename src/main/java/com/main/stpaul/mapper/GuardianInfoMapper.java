package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.request.GuardianInfoRequest;
import com.main.stpaul.dto.response.GuardianInfoResponse;
import com.main.stpaul.entities.GuardianInfo;

@Component
public class GuardianInfoMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public GuardianInfo toGuardianInfo(GuardianInfoRequest guardianInfo){
        return this.modelMapper.map(guardianInfo,GuardianInfo.class);
    }

    public GuardianInfoResponse toGuardianInfoResponse(GuardianInfo guardianInfo){
        return this.modelMapper.map(guardianInfo,GuardianInfoResponse.class);
    }
}
