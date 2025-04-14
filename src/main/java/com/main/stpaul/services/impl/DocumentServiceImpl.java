package com.main.stpaul.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.response.DocumentReponse;
import com.main.stpaul.entities.Documents;
import com.main.stpaul.repository.DocumentRepo;
import com.main.stpaul.services.serviceInterface.DocumentsService;

@Service
public class DocumentServiceImpl implements DocumentsService{

    @Autowired
    private DocumentRepo documentRepo;

    @Override
    public Documents addDocuments(Documents documents) {
        return this.documentRepo.save(documents);
    }

    @Override
    public List<DocumentReponse> getStudentDocuments(String studentid) {
        return this.documentRepo.findAlldocuments(studentid);
    }

    @Override
    public void deleteDocument(long id) {
        this.documentRepo.deleteDocumentById(id);
        return;
    }

    @Override
    public void updateDocument(byte[] document, long id) {
        this.documentRepo.updateDocument(document, id);
        return;
    }
    
}
