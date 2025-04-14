package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.constants.Status;
import com.main.stpaul.entities.StudentAcademics;

import jakarta.transaction.Transactional;

public interface StudentAcademicsRepo extends JpaRepository<StudentAcademics,String> {
    
    
    @Query("SELECT s FROM StudentAcademics s WHERE s.student.id=:id AND s.isDelete=false ORDER BY s.addDate DESC")
    List<StudentAcademics> findByStudent(long id);

    @Query("SELECT s FROM StudentAcademics s WHERE s.studentAcademicsId=:id AND s.isDelete=false")
    Optional<StudentAcademics> findById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE StudentAcademics s SET s.isDelete=true AND s.deleteDate=now WHERE s.studentAcademicsId=:id")
    void deleteStudentAcademics(long id);

    @Query("""
        SELECT s
        FROM StudentAcademics s
        WHERE s.student.id = :studentId AND s.isDelete = false
        ORDER BY s.addDate DESC
    """)
    List<StudentAcademics> findByStudentId(@Param("studentId") String studentId);

    @Query("""
        SELECT s
        FROM StudentAcademics s
        WHERE s.studentAcademicsId = :id AND s.isDelete = false
    """)
    Optional<StudentAcademics> findAcademicsById(String id);

    @Query("""
        SELECT s
        FROM StudentAcademics s
        WHERE s.student.id = :studentId AND s.isDelete = false AND s.status=:status
    """)
    Optional<StudentAcademics> findOngoingAcademicsByStudent(@Param("studentId") String studentId,@Param("status")Status status);

    @Query("""
        SELECT s
        FROM StudentAcademics s
        WHERE s.student.id = :studentId AND s.isDelete = false AND s.status=:status
    """)
    Optional<StudentAcademics> findPendingAcademicsByStudent(@Param("studentId") String studentId,@Param("status")Status status);

}
