package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.request.CollegeFeesRequest;
import com.main.stpaul.entities.CollegeFees;

@Component
public class CollegeFeesMappler {
    
    @Autowired
    private ModelMapper modelMapper;

    public CollegeFees toCollegeFees(CollegeFeesRequest request){
        return this.modelMapper.map(request, CollegeFees.class);
    }
}
