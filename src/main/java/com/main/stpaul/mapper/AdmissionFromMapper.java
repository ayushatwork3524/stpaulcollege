package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.request.AdmissionFormRequest;
import com.main.stpaul.entities.AdmissionForm;

@Component 
public class AdmissionFromMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public AdmissionForm toAdmissionForm(AdmissionFormRequest admissionForm){
        return this.modelMapper.map(admissionForm, AdmissionForm.class);
    }
}
