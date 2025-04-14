package com.main.stpaul.services.serviceInterface;

import java.util.List;

import com.main.stpaul.dto.request.CollegeFeesRequest;
import com.main.stpaul.entities.CollegeFees;

public interface CollegeFeesService {
    CollegeFees addCollegeFees(CollegeFees collegeFees);
    List<CollegeFees> getAllCollegeFees(String stdClass);
    CollegeFees getCollegeFees(long id);
    CollegeFees getCollegeFeesByClass(String Class);
    void updateCollegeFees(CollegeFeesRequest collegeFees,long id);
    void deleteCollegeFees(long id);
    double getTotalFeesByClass(String stdClass);
    List<String> distinctClasses();
}
