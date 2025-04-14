package com.main.stpaul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.dto.response.DocumentReponse;
import com.main.stpaul.entities.Documents;

import jakarta.transaction.Transactional;

public interface DocumentRepo extends JpaRepository<Documents,Long>{
    

    @Query("""
            SELECT new com.main.stpaul.dto.response.DocumentReponse( d.documentId,d.documentType,d.document)
            FROM Documents d
            WHERE d.student.id=:id AND d.isDelete=false
            """)
    List<DocumentReponse> findAlldocuments(@Param("id")String id);

    @Transactional
    @Modifying
    @Query("UPDATE Documents d SET d.isDelete=true , d.deleteDate=now WHERE d.documentId=:id")
    void deleteDocumentById(long id);

    @Transactional
    @Modifying
    @Query("""
        UPDATE Documents d SET d.document=:document,d.updatedDate=now
        WHERE d.documentId=:id
        """)
    void updateDocument(@Param("document")byte[] document,
                        @Param("id")long id);

}
