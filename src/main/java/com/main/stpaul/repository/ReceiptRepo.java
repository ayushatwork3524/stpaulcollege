package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.entities.Receipt;

public interface ReceiptRepo extends JpaRepository<Receipt,Long>{

    Receipt findByReceiptNo(Long receiptNo);
    
    @Query("""
            SELECT r
            FROM Receipt r
            WHERE r.paymentDetail.paymentDetailId=:pdId AND r.isDelete=false
            """)
    List<Receipt> findByPaymentDetail(String pdId);

    @Query("""
        SELECT r
        FROM Receipt r
        WHERE r.receiptNo=:id AND r.isDelete=false
        """)
    Optional<Receipt> findByReceiptId(String id);
}
