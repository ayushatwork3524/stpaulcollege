package com.main.stpaul.services.serviceInterface;

import java.util.List;

import com.main.stpaul.dto.response.DocumentReponse;
import com.main.stpaul.entities.Documents;

public interface DocumentsService {
    Documents addDocuments(Documents documents);
    List<DocumentReponse> getStudentDocuments(String studentid);
    void deleteDocument(long id);
    void updateDocument(byte[] document,long id);
}
