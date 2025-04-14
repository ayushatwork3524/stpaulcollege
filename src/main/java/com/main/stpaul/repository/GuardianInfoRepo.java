package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.entities.GuardianInfo;

import jakarta.transaction.Transactional;

public interface GuardianInfoRepo extends JpaRepository<GuardianInfo,String>{
    
    @Query("SELECT g FROM GuardianInfo g WHERE g.student.id = :studentId AND g.isDelete = false")
    Optional<GuardianInfo> findByStudentId(String studentId);

    @Transactional
    @Modifying
    @Query("UPDATE GuardianInfo g SET g.isDelete=true AND g.deleteDate=now WHERE g.giId=:id")
    void deleteGuardianInfo(String id);

    @Transactional
    @Modifying
    @Query("""
        UPDATE GuardianInfo g SET g.updatedDate=now ,g.name=:name,g.phone=:phone,g.occupation=:occupation,g.relation=:relation,
        g.income=:income
        WHERE g.giId=:id AND g.isDelete=false
        """)
    void updateGuardianInfo(@Param("name")String name,@Param("phone")String phone,
                            @Param("occupation")String occupation,@Param("relation")String relation,
                            @Param("income")double income,@Param("id")String id);
}
