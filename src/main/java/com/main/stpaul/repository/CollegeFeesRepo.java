package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.entities.CollegeFees;
import jakarta.transaction.Transactional;

public interface CollegeFeesRepo extends JpaRepository<CollegeFees,Long>{
    
    @Query("""
        SELECT c FROM CollegeFees c
        WHERE c.isDelete=false
        AND (:stdClass IS NULL OR c.stdClass=:stdClass)
        """)
    List<CollegeFees> getAllCollegeFees(@Param("stdClass") String stdClass);

    @Query("""
        SELECT c
        FROM CollegeFees c
        WHERE c.isDelete=false AND c.id=:id
        """)
    Optional<CollegeFees> findCollegeFeesById(Long id);

    @Transactional
    @Modifying
    @Query("""
        UPDATE CollegeFees c Set c.stdClass=:stdClass,c.totalFees=:totalFees,c.installmentGap=:installmentGap,
        c.installmentsAmount=:installmentsAmount,c.installments=:installments
        WHERE c.id=:id            
            """)
    void updateCollegeFees(@Param("stdClass")String stdClass,
                            @Param("totalFees")double totalFees,
                            @Param("installmentGap")int installmentGap,
                            @Param("installmentsAmount")double installmentsAmount,
                            @Param("installments")long installments,
                            @Param("id")long id
                            );

    @Transactional
    @Modifying
    @Query("""
        UPDATE CollegeFees c Set c.isDelete=true ,c.deleteDate=now
        WHERE c.id=:id            
            """)
    void deleteCollegeFees(@Param("id")long id);

    @Query("SELECT c.totalFees FROM CollegeFees c WHERE c.stdClass=:stdClass and c.isDelete=false")
    Optional<Double> getFeesByClass(@Param("stdClass") String stdClass);

    @Query("""
        SELECT c 
        FROM CollegeFees c
        WHERE c.stdClass=:stdClass and c.isDelete=false
        """)
    Optional<CollegeFees> getCollegefeesFeesByClass(@Param("stdClass") String stdClass);


    @Query("SELECT DISTINCT c.stdClass From CollegeFees c WHERE c.isDelete = false")
    List<String> distinctClasses();


}
