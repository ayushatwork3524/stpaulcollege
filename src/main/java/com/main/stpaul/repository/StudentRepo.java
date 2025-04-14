package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.constants.Result;
import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.response.PendingStudents;
import com.main.stpaul.entities.Student;

import jakarta.transaction.Transactional;

public interface StudentRepo extends JpaRepository<Student,String>{
    
    @Query("""
        SELECT s FROM Student s 
        WHERE (:query IS NULL OR (s.firstName LIKE %:query% OR s.fatherName LIKE %:query% OR s.surname LIKE %:query% OR s.email LIKE %:query% OR s.phoneNo LIKE %:query%))
        AND (:stdClass IS NULL OR s.stdClass=:stdClass)
        AND (:section IS NULL OR s.section=:section)
        AND (:session IS NULL OR s.session=:session)
        AND s.isDelete = false 
        ORDER BY s.addDate DESC
       """)
    List<Student> findAllStudents(@Param("query")String query,@Param("stdClass")String stdClass,@Param("section")String section,
                                    @Param("session")String session);


    @Query("""
        SELECT s 
        FROM Student s WHERE s.studentId=:id
        AND s.isDelete = false
        ORDER BY s.addDate DESC
       """)
    Optional<Student> findByStudentId(@Param("id")String id);

    @Query("""
            SELECT new com.main.stpaul.dto.response.PendingStudents(s.studentId, s.firstName, s.fatherName, s.surname,
            s.email, s.phoneNo,s.gender, s.dateOfBirth, s.admissionDate,  sa.studentAcademicsId,
            s.session, s.stdClass, s.status,0.0)
            FROM Student s 
            LEFT JOIN studentAcademics sa
            ON s.studentId=sa.student.studentId AND sa.isDelete = false
            WHERE(:query IS NULL OR (s.firstName LIKE %:query% OR s.fatherName LIKE %:query% OR s.surname LIKE %:query% OR s.email LIKE %:query% OR s.phoneNo LIKE %:query%))
            AND (:stdClass IS NULL OR s.stdClass=:stdClass)
            AND (:section IS NULL OR s.section=:section)
            AND (:session IS NULL OR s.session=:session)
            AND sa.status = :status 
            AND s.isDelete = false
            ORDER BY s.addDate DESC
            """)
    List<PendingStudents> findByStatus(@Param("query")String query,@Param("stdClass")String stdClass,@Param("section")String section,
                                        @Param("session")String session,@Param("status") Status status);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.email = :email AND s.isDelete = false")
    boolean existsByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.deleteDate=now , s.isDelete=true WHERE s.studentId=:id")
    void deleteStudent(String id);


    @Query("""
        SELECT s 
        FROM Student s 
        LEFT JOIN StudentAcademics sa 
        ON s.studentId = sa.student.studentId
        WHERE (:query IS NULL OR (s.firstName LIKE %:query% OR s.fatherName LIKE %:query% OR s.surname LIKE %:query% OR s.email LIKE %:query% OR s.phoneNo LIKE %:query%))
        AND (:stdClass IS NULL OR s.stdClass=:stdClass)
        AND (:section IS NULL OR s.section=:section)
        AND (:session IS NULL OR s.session=:session)
        AND s.isDelete = false AND sa.result=:fail
        ORDER BY s.addDate DESC
       """)
    List<Student> findFailStudents(@Param("query")String query,@Param("stdClass")String stdClass,@Param("section")String section,
                                    @Param("session")String session,Result fail);

}
