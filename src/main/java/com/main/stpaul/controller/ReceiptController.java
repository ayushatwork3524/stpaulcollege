// package com.main.stpaul.controller;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;

// import org.springframework.http.ContentDisposition;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.main.stpaul.helper.PdfGenerator;

// @RestController
// @RequestMapping("/api/auth/receipt")
// public class ReceiptController {

// @GetMapping()
// public ResponseEntity<?> generateReceipt() throws IOException {

// ByteArrayOutputStream bos = PdfGenerator.generateReceipt();

// HttpHeaders headers = new HttpHeaders();
// headers.setContentType(MediaType.APPLICATION_PDF);
// headers.setContentDisposition(ContentDisposition.builder("attachment")
// .filename("leaving_certificate.pdf")
// .build());

// return new ResponseEntity<>(bos.toByteArray(), headers, HttpStatus.OK);
// }

// }
