// package com.main.stpaul.controller;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.time.LocalDateTime;

// import org.springframework.http.ContentDisposition;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.main.stpaul.entities.Student;
// import com.main.stpaul.entities.TransferCertificate;
// import com.main.stpaul.repository.StudentRepo;
// import com.main.stpaul.repository.LeavingCertificateRepository;
// import com.main.stpaul.services.serviceInterface.StudentService;
// import com.main.stpaul.services.serviceInterface.LCService;

// @RestController
// @RequestMapping("/api/manager/lc")
// public class LeavingCertificateController {

// private final LeavingCertificateRepository transferCertificateRepository;

// private final LCService lcService;

// private final StudentService studentService;

// private final StudentRepo studentRepo;

// public LeavingCertificateController(LeavingCertificateRepository
// transferCertificateRepository,
// LCService lcService, StudentService studentService, StudentRepo studentRepo)
// {
// this.transferCertificateRepository = transferCertificateRepository;
// this.lcService = lcService;
// this.studentService = studentService;
// this.studentRepo = studentRepo;
// }

// @GetMapping("/request")
// public ResponseEntity<?> generateLc(@RequestParam String studentId) throws
// IOException {
// Student student = this.studentService.getStudentById(studentId);

// if (student == null) {
// return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
// }

// ByteArrayOutputStream bos = this.lcService.generateLc(studentId);

// int copyCount = student.getLeavingCertificates() == null ? 0 :
// student.getLeavingCertificates().size();

// TransferCertificate transferCertificate = TransferCertificate.builder()
// .student(student)
// .copyNumber(copyCount)
// .data(bos.toByteArray())
// .issueAt(LocalDateTime.now().toString())
// .build();

// this.transferCertificateRepository.save(transferCertificate);

// if (student.getLeavingCertificates() != null) {
// student.getLeavingCertificates().add(transferCertificate);
// this.studentRepo.save(student);
// }

// HttpHeaders headers = new HttpHeaders();
// headers.setContentType(MediaType.APPLICATION_PDF);
// headers.setContentDisposition(ContentDisposition.builder("attachment")
// .filename("leaving_certificate.pdf")
// .build());

// return new ResponseEntity<>(bos.toByteArray(), headers, HttpStatus.OK);
// }

// }
