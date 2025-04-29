package com.main.stpaul.services.serviceInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.main.stpaul.dto.request.TransferCertificateRequest;
import com.main.stpaul.entities.Student;

public interface TCService {
    ByteArrayOutputStream generateTc(Student student, TransferCertificateRequest req) throws IOException;
}
