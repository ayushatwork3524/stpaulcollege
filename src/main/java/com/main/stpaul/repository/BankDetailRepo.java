package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.main.stpaul.entities.BankDetail;

import jakarta.transaction.Transactional;

public interface BankDetailRepo extends JpaRepository<BankDetail,String>{
    

    @Query("SELECT b FROM BankDetail b Where b.student.id=:studentId AND b.isDelete=false")
    Optional<BankDetail> findByStudentId(String studentId);


    @Transactional
    @Modifying
    @Query("UPDATE BankDetail b SET b.isDelete=true AND b.deleteDate=now WHERE b.bankDetailId=:id")
    void deleteBankDetail(String id);

    @Transactional
    @Modifying
    @Query("""
        UPDATE BankDetail b SET b.bankName = :bankName, b.branchName = :branchName, 
        b.accountNo = :accountNo, b.ifscCode = :ifscCode, b.updatedDate=now
        WHERE b.bankDetailId = :id
        """)
    void updateBankDetail(
        @Param("bankName") String bankName,
        @Param("branchName") String branchName,
        @Param("accountNo") String accountNo,
        @Param("ifscCode") String ifscCode,
        @Param("id") String id
    );
    }
