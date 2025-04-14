package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.dto.request.GuardianInfoRequest;
import com.main.stpaul.entities.GuardianInfo;

public interface GuardianInfoService {
    
    GuardianInfo addGuardianInfo(GuardianInfo guardianInfo);
    GuardianInfo getGuardianInfoById(String id);
    GuardianInfo getGuardianInfoByStudent(String studentId);
    boolean updateGuardianInfo(GuardianInfoRequest guardianInfoRequest,String id);
    boolean deleteGuardianInfo(String id);
}
