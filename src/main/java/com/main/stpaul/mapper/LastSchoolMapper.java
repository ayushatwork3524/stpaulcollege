package com.main.stpaul.mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.request.LastSchoolRequest;
import com.main.stpaul.dto.response.LastSchoolResponse;
import com.main.stpaul.entities.LastSchool;

@Component
public class LastSchoolMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public LastSchool toLastSchool(LastSchoolRequest lastSchool){
        return this.modelMapper.map(lastSchool, LastSchool.class);
    }

    public LastSchoolResponse toLastSchoolResponse(LastSchool lastSchool){
        return this.modelMapper.map(lastSchool, LastSchoolResponse.class);
    }
}
