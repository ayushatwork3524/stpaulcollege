package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.dto.request.LastSchoolRequest;
import com.main.stpaul.entities.LastSchool;

public interface LastSchoolService {

    LastSchool addLastSchool(LastSchool lastSchool);
    LastSchool getLastSchoolById(String id);
    LastSchool getLastSchoolByStudent(String studentId);
    void updateLastSchool(LastSchoolRequest lastSchool,String id);
    void deleteLastSchool(String lsId);
    
}
