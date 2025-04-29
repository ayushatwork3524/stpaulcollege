package com.main.stpaul.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.dto.request.TransferCertificateRequest;
import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.TransferCertificate;
import com.main.stpaul.repository.StudentRepo;
import com.main.stpaul.repository.TransferCertificateRepository;
import com.main.stpaul.services.serviceInterface.StudentService;
import com.main.stpaul.services.serviceInterface.TCService;

@RestController
@RequestMapping("/api/manager/tc")
public class TransferCertificateController {

    private final TCService tcService;

    private final StudentService studentService;

    private final TransferCertificateRepository transferCertificateRepository;

    private final StudentRepo studentRepo;

    public TransferCertificateController(TCService tcService, StudentService studentService,
            TransferCertificateRepository transferCertificateRepository, StudentRepo studentRepo) {
        this.tcService = tcService;
        this.studentService = studentService;
        this.transferCertificateRepository = transferCertificateRepository;
        this.studentRepo = studentRepo;
    }

    @PostMapping("/request/{sId}")
    public ResponseEntity<?> generateTc(@PathVariable String sId,
            @RequestBody(required = false) TransferCertificateRequest req)
            throws IOException {

        Student student = this.studentService.getStudentById(sId);

        if (student == null) {
            SuccessResponse res = SuccessResponse
                    .builder()
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(404)
                    .message("Student not found")
                    .build();
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }

        if (student.getTransferCertificate() != null) {
            TransferCertificate transferCertificate = student.getTransferCertificate();
            transferCertificate.setCopyNumber(transferCertificate.getCopyNumber() + 1);
            this.transferCertificateRepository.save(transferCertificate);
            DataResponse res = DataResponse
                    .builder()
                    .status(HttpStatus.ALREADY_REPORTED)
                    .statusCode(208)
                    .data(student.getTransferCertificate().getData())
                    .message("Certificate already generated")
                    .build();
            return new ResponseEntity<>(res, HttpStatus.ALREADY_REPORTED);
        }

        ByteArrayOutputStream bos = this.tcService.generateTc(student, req);

        if (req.getTcNo() != null) {

            TransferCertificate transferCertificate = TransferCertificate.builder()
                    .student(student)
                    .copyNumber(student.getTransferCertificate() == null ? 1
                            : student.getTransferCertificate().getCopyNumber() + 1)
                    .data(bos.toByteArray())
                    .issueAt(LocalDateTime.now().toString())
                    .build();

            this.transferCertificateRepository.save(transferCertificate);

            if (student.getTransferCertificate() != null) {
                student.setTransferCertificate(transferCertificate);
                this.studentRepo.save(student);
            }
        }

        DataResponse res = DataResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Transfer Certificate generated successfully")
                .statusCode(200)
                .data(bos.toByteArray())
                .build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
