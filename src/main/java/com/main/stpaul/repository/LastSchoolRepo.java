package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.entities.LastSchool;

import jakarta.transaction.Transactional;

public interface LastSchoolRepo extends JpaRepository<LastSchool,String> {
    
    @Query("SELECT l FROM LastSchool l WHERE l.student.id = :id AND l.isDelete = false")
    Optional<LastSchool> findByStudent(String id);
    
    @Transactional
    @Modifying
    @Query("UPDATE LastSchool l SET l.isDelete=true AND l.deleteDate=now WHERE l.lsId=:id")
    void deleteLastSchool(String id);

    @Transactional
    @Modifying
    @Query("""
            UPDATE LastSchool l SET l.collegeName=:collegeName,l.lastStudentId=:studentId,l.rollNo=:rollNo,
            l.uid=:uid,l.examination=:examination,l.examMonth=:examMonth,l.marksObtained=:marksObtained,
            l.result=:result , l.updatedDate=now
            WHERE l.lsId=:id AND l.isDelete=false
            """)
    void updateLastSchool(@Param("collegeName")String collegeName,@Param("studentId")String studentId,
                            @Param("rollNo")String rollNo,@Param("uid")String uid,@Param("examination")String examination,
                            @Param("examMonth")String examMonth,@Param("marksObtained")int marksObtained,
                            @Param("result")String result,@Param("id")String id);


}
