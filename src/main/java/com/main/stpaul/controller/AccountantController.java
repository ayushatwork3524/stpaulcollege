package com.main.stpaul.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.request.AddPaymentRequest;
import com.main.stpaul.dto.response.PendingStudents;
import com.main.stpaul.entities.PaymentDetail;
import com.main.stpaul.entities.Receipt;
import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.helper.PdfGenerator;
import com.main.stpaul.mapper.ReceiptMapper;
import com.main.stpaul.services.impl.CollegeFeesServiceImpl;
import com.main.stpaul.services.impl.PaymentDetailServiceImpl;
import com.main.stpaul.services.impl.ReceiptServiceImpl;
import com.main.stpaul.services.impl.StudentAcademicsServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/accountant")
public class AccountantController {
    

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private ReceiptServiceImpl receiptServiceImpl;

    @Autowired
    private PaymentDetailServiceImpl paymentDetailServiceImpl;

    @Autowired
    private StudentAcademicsServiceImpl studentAcademicsServiceImpl;

    @Autowired
    private ReceiptMapper receiptMapper;

    @Autowired
    private CollegeFeesServiceImpl collegeFeesServiceImpl;


    @PostMapping("/student/academics/{academicsId}/payment")
    public ResponseEntity<?> addPayment(
            @PathVariable("academicsId") String academicsId,
            @RequestBody AddPaymentRequest addPaymentRequest) throws Exception {

        log.info("Starting addPayment method with academicsId: {}", academicsId);
        
        try {
            // 1. Fetch StudentAcademics
            StudentAcademics studentAcademics = studentAcademicsServiceImpl.getAcademicsById(academicsId);
            if (studentAcademics == null) {
                log.error("Student Academics not found with ID: {}", academicsId);
                throw new EntityNotFoundException("Student Academics not found!");
            }

            // 2. Fetch PaymentDetail
            PaymentDetail paymentDetail = studentAcademics.getPaymentDetail();
            if (paymentDetail == null) {
                log.error("Payment Detail not found for student with academicsId: {}", academicsId);
                throw new EntityNotFoundException("Payment Detail not found!");
            }

            // 3. Fetch Student
            Student student = studentServiceImpl.getStudentById(studentAcademics.getStudent().getStudentId());
            if (student == null) {
                log.error("Student not found with ID: {}", studentAcademics.getStudent().getStudentId());
                throw new EntityNotFoundException("Student not found!");
            }

            // 4. Update PaymentDetail
            double updatedPaidAmount = paymentDetail.getPaidAmount() + addPaymentRequest.getAmountPaid();
            paymentDetail.setPaidAmount(updatedPaidAmount);
            paymentDetail.setBalanceAmount(paymentDetail.getTotalFees() - updatedPaidAmount);
            paymentDetail.setUpdatedDate(LocalDateTime.now()); // fix: update the timestamp
            if (paymentDetail.getInstallments() > 0) {
                paymentDetail.setInstallments(paymentDetail.getInstallments() - 1);
            }
            if (paymentDetail.getDueDate() != null) {
                paymentDetail.setDueDate(LocalDate.now().plusDays(paymentDetail.getInstallmentGap()));
            }

            // 6. Create Receipt
            Receipt newReceipt = new Receipt();
            newReceipt.setAmountPaid(addPaymentRequest.getAmountPaid());
            newReceipt.setPaymentMode(addPaymentRequest.getPaymentMode());
            newReceipt.setPaymentDate(LocalDateTime.now());

            // 7. Persist changes
            paymentDetail = paymentDetailServiceImpl.updatePaymentDetail(paymentDetail);
            newReceipt.setPaymentDetail(paymentDetail);
            newReceipt = receiptServiceImpl.addReceipt(newReceipt);

            // 8. Generate PDF
            byte[] pdf = PdfGenerator.generateReceiptPdf(
                    student,
                    receiptMapper.toReceiptResponse(newReceipt),
                    paymentDetail
            );
            

            // 9. Build response
            DataResponse response = DataResponse.builder()
                    .data(pdf)
                    .message("Payment added successfully!")
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();

            log.info("Successfully added payment for student with academicsId: {}", academicsId);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            log.error("Error adding payment for student with academicsId: {}: {}", academicsId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    
    @GetMapping("/student/{studentId}/payment/receipt/{receiptId}")
    public ResponseEntity<?> downloadPdf(@PathVariable("studentId")String studentId,@PathVariable("receiptId")Long receiptId)throws Exception{
        log.info("Starting downloadPdf method with studentId: {} and receiptId: {}", studentId, receiptId);
        try {
            Student student = this.studentServiceImpl.getStudentById(studentId);
            if(student ==null){
                log.error("Student not found with ID: {}", studentId);
                throw new EntityNotFoundException("Student not found !");
            }
            Receipt receipt = this.receiptServiceImpl.findByid(receiptId);
            String id = receipt.getPaymentDetail().getPaymentDetailId();

            byte[] pdf = PdfGenerator.generateReceiptPdf(student,this.receiptMapper.toReceiptResponse(receipt),this.paymentDetailServiceImpl.getPaymentById(id));

            DataResponse response = DataResponse.builder()
                                                .data(pdf)
                                                .message("payment Receipt get Successfully !")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            log.info("Successfully generated PDF for studentId: {} and receiptId: {}", studentId, receiptId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error generating PDF for studentId: {} and receiptId: {}: {}", studentId, receiptId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/student/pending")
    public ResponseEntity<?> getPendingStudents(@RequestParam(required = false) String query,
                                                @RequestParam(required = false) String stdClass,
                                                @RequestParam(required = false) String section,
                                                @RequestParam(required = false) String session)throws Exception{
        log.info("Starting getPendingStudents method");
        try {
            List<PendingStudents> students = this.studentServiceImpl.getPendingStudents(query, stdClass, section, session); 
            for(PendingStudents student:students){
                student.setTotalFees(this.collegeFeesServiceImpl.getTotalFeesByClass(student.getStdClass()));
            }
            DataResponse response = DataResponse.builder()
                                                .data(students)
                                                .message("get All Pending Students successfully !")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            log.info("Successfully retrieved pending students");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error retrieving pending students: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/student/payment-detail/{paymentDetailId}")
    public ResponseEntity<?> deletePaymentDetail(@PathVariable("paymentDetailId")String paymentDetailId)throws Exception{
        log.info("Starting deletePaymentDetail method with paymentDetailId: {}", paymentDetailId);
        try {
            this.paymentDetailServiceImpl.deletePaymentDetail(paymentDetailId);
            DataResponse response = DataResponse.builder()
                                                .message("Payment Detail deleted successfully !")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            log.info("Successfully deleted payment detail with ID: {}", paymentDetailId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting payment detail with ID: {}: {}", paymentDetailId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}