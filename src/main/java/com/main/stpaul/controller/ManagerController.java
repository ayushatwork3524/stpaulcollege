package com.main.stpaul.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.main.stpaul.constants.PaymentMode;
import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.dto.request.BankDetailRequest;
import com.main.stpaul.dto.request.BioFocalSubjectRequest;
import com.main.stpaul.dto.request.GuardianInfoRequest;
import com.main.stpaul.dto.request.LastSchoolRequest;
import com.main.stpaul.dto.request.PaymentDetailRequest;
import com.main.stpaul.dto.request.StreamRequest;
import com.main.stpaul.dto.request.StudentAddRequest;
import com.main.stpaul.dto.request.StudentRequest;
import com.main.stpaul.dto.request.UpdateAcademicsRequest;
import com.main.stpaul.dto.response.DocumentReponse;
import com.main.stpaul.dto.response.StudentDetailResponse;
import com.main.stpaul.entities.AdmissionForm;
import com.main.stpaul.entities.BankDetail;
import com.main.stpaul.entities.BiofocalSubject;
import com.main.stpaul.entities.Documents;
import com.main.stpaul.entities.GuardianInfo;
import com.main.stpaul.entities.LastSchool;
import com.main.stpaul.entities.PaymentDetail;
import com.main.stpaul.entities.Receipt;
import com.main.stpaul.entities.Stream;
import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.entities.Subject;
import com.main.stpaul.helper.PdfGenerator;
import com.main.stpaul.mapper.AdmissionFromMapper;
import com.main.stpaul.mapper.BankDetailMapper;
import com.main.stpaul.mapper.GuardianInfoMapper;
import com.main.stpaul.mapper.LastSchoolMapper;
import com.main.stpaul.mapper.PaymentDetailMapper;
import com.main.stpaul.mapper.ReceiptMapper;
import com.main.stpaul.mapper.StudentAcademicsMapper;
import com.main.stpaul.mapper.StudentMapper;

import com.main.stpaul.services.impl.AdmissionFormServiceImpl;
import com.main.stpaul.services.impl.BankDetailServiceImpl;
import com.main.stpaul.services.impl.BioFocalSubjectServiceImpl;
import com.main.stpaul.services.impl.DocumentServiceImpl;
import com.main.stpaul.services.impl.GuardianInfoServiceImpl;
import com.main.stpaul.services.impl.LastSchoolServiceImpl;
import com.main.stpaul.services.impl.PaymentDetailServiceImpl;
import com.main.stpaul.services.impl.ReceiptServiceImpl;
import com.main.stpaul.services.impl.StreamServiceImpl;
import com.main.stpaul.services.impl.StudentAcademicsServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;
import com.main.stpaul.services.impl.SubjectServiceImpl;
import com.main.stpaul.services.serviceInterface.ExcelService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private LastSchoolServiceImpl lastSchoolServiceImpl;

    @Autowired
    private BankDetailServiceImpl bankDetailServiceImpl;

    @Autowired
    private GuardianInfoServiceImpl guardianInfoServiceImpl;

    @Autowired
    private AdmissionFormServiceImpl admissionFormServiceImpl;

    @Autowired
    private StudentAcademicsServiceImpl studentAcademicsServiceImpl;

    @Autowired
    private SubjectServiceImpl subjectServiceImpl;

    @Autowired
    private ReceiptServiceImpl receiptServiceImpl;

    @Autowired
    private StreamServiceImpl streamServiceImpl;

    @Autowired
    private DocumentServiceImpl documentServiceImpl;

    @Autowired
    private PaymentDetailServiceImpl paymentDetailServiceImpl;

    @Autowired
    private BioFocalSubjectServiceImpl bioFocalSubjectServiceImpl;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private BankDetailMapper bankDetailMapper;

    @Autowired
    private LastSchoolMapper lastSchoolMapper;

    @Autowired
    private GuardianInfoMapper guardianInfoMapper;

    @Autowired
    private AdmissionFromMapper admissionFromMapper;

    @Autowired
    private PaymentDetailMapper paymentDetailMapper;

    @Autowired
    private StudentAcademicsMapper studentAcademicsMapper;

    @Autowired
    private ReceiptMapper receiptMapper;

    @Autowired
    private DocumentServiceImpl documentService;

    @Autowired
    private ExcelService excelService;

    // Post Apis *********************

    @PostMapping("/student")
    @Operation(summary = "Register a new student", description = "Registers a new student with the provided details and optional image")
    public ResponseEntity<?> registerStudent(@RequestPart("studentAdd") StudentAddRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        log.info("Starting registerStudent method");
        try {
            Student student = this.studentMapper.toStudent(request.getStudent());
            student.setSession(request.getAdmissionForm().getSession());
            student.setAdmissionDate(request.getAdmissionForm().getAdmissionDate());
            student.setStdClass(request.getAdmissionForm().getStdClass());
            student.setLocalAddress(request.getStudent().getLocalAddress());
            student.setPermanentAddress(request.getStudent().getPermanentAddress());
            try {
                student.setImage(image.getBytes());
            } catch (Exception e) {
                student.setImage(null);
            }
            student = this.studentServiceImpl.addStudent(student);
            log.info("Student Added Successfully ");

            AdmissionForm admissionForm = this.admissionFromMapper.toAdmissionForm(request.getAdmissionForm());
            admissionForm.setStudent(student);
            this.admissionFormServiceImpl.addAdmissionForm(admissionForm);
            log.info("admission form Added Successfully ");

            // BankDetail bankDetail =
            // this.bankDetailMapper.toBankDetail(request.getBankDetail());
            // bankDetail.setStudent(student);
            // this.bankDetailServiceImpl.addBankDetail(bankDetail);
            // log.info("Bank Detail Added Successfully ");

            LastSchool lastSchool = this.lastSchoolMapper.toLastSchool(request.getLastSchool());
            lastSchool.setStudent(student);
            this.lastSchoolServiceImpl.addLastSchool(lastSchool);
            log.info("Last School Detail Added Successfully ");

            GuardianInfo guardianInfo = this.guardianInfoMapper.toGuardianInfo(request.getGuardianInfo());
            guardianInfo.setStudent(student);
            this.guardianInfoServiceImpl.addGuardianInfo(guardianInfo);
            log.info("Guardian Into Added Successfully ");

            StudentAcademics studentAcademics = new StudentAcademics();
            studentAcademics.setStdClass(admissionForm.getStdClass());
            studentAcademics.setStudent(student);
            this.studentAcademicsServiceImpl.addStudentAcademics(studentAcademics);
            log.info("Academic Detail Added Successfully ");

            Stream stream = new Stream();
            stream.setAcademics(studentAcademics);
            stream.setMedium(request.getSubject().getMedium());
            stream.setStream(request.getSubject().getStream());
            stream.setSubStream(request.getSubject().getSubStream());
            stream = this.streamServiceImpl.addStream(stream);
            for (String subject : request.getSubject().getSubjects()) {
                Subject sb = new Subject();
                sb.setName(subject);
                sb.setStream(stream);
                this.subjectServiceImpl.addSubject(sb);
            }
            log.info("Subjects Added Successfully ");

            if (request.getBioFocalSubject() != null) {
                BiofocalSubject biofocalSubject = new BiofocalSubject();
                biofocalSubject.setMedium(request.getBioFocalSubject().getMedium());
                biofocalSubject.setAcademics(studentAcademics);
                biofocalSubject.setSubStream(request.getBioFocalSubject().getSubStream());
                biofocalSubject.setSubject(request.getBioFocalSubject().getSubject());
                this.bioFocalSubjectServiceImpl.addBiofocalSubject(biofocalSubject);
                log.info("Bio Focal Subject Added Successfully ");
            }

            DataResponse response = DataResponse.builder()
                    .data(student.getStudentId())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("Student Register Successfully !")
                    .build();
            log.info("Student Admission Successfully ");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error Occured While addmision of Student");
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/students/{id}/documents")
    @Operation(summary = "Upload student documents", description = "Uploads documents for a specific student by their ID")
    public ResponseEntity<?> uploadDoucuments(@PathVariable("id") String id,
            @RequestParam Map<String, MultipartFile> files) throws Exception {
        log.info("Starting uploadDoucuments method with studentId: {}", id);
        log.info("Uploading Student Documents");
        Student student = this.studentServiceImpl.getStudentById(id);
        StudentAcademics studnetAcademics = this.studentAcademicsServiceImpl.getPendingAcademicsByStudent(id);
        if (student == null) {
            log.warn("student not found for id : {}", id);
            throw new EntityNotFoundException("Student not present !");
        }
        if (studnetAcademics == null) {
            log.warn("student academics not found for id : {}", id);
            throw new EntityNotFoundException("Student Academics not present !");
        }

        files.forEach((docName, file) -> {
            try {
                Documents document = new Documents();
                document.setDocumentType(docName);
                document.setDocument(file.getBytes());
                document.setStudent(student);
                this.documentServiceImpl.addDocuments(document);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        studnetAcademics.setStatus(Status.Pending);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "document uploaded Successfully !");
        log.info("document uploaded Successfully ");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/students/{studentId}/academics/{academicId}/payment-detail")
    @Operation(summary = "Add payment detail", description = "Adds payment details for a specific student and academic record")
    public ResponseEntity<?> addPaymentDetail(@PathVariable("studentId") String studentId,
            @PathVariable("academicId") String academicId, @RequestBody PaymentDetailRequest paymentDetail)
            throws Exception {
        log.info("Starting addPaymentDetail method with studentId: {} and academicId: {}", studentId, academicId);
        try {
            StudentAcademics academics = this.studentAcademicsServiceImpl.getAcademicsById(academicId);
            if (academics == null) {
                log.warn("student Academics not found with id : {}", academicId);
                throw new EntityNotFoundException("Student Academics not present !");
            }
            PaymentDetail paymentDetail2 = this.paymentDetailMapper.toPaymentDetail(paymentDetail);
            paymentDetail2.setStudentAcademics(academics);
            Receipt receipt = new Receipt();

            receipt.setAmountPaid(paymentDetail2.getPaidAmount());
            receipt.setTransactionId(null);
            receipt.setPaymentMode(PaymentMode.valueOf(paymentDetail2.getPaymentType()));
            receipt.setPaymentDate(LocalDateTime.now());

            paymentDetail2 = this.paymentDetailServiceImpl.addPaymentDetail(paymentDetail2);
            receipt.setPaymentDetail(paymentDetail2);
            receipt = this.receiptServiceImpl.addReceipt(receipt);

            Student student = this.studentServiceImpl.getStudentById(studentId);
            student.setStatus(Status.Ongoing);
            this.studentServiceImpl.updateStudent(student);
            academics.setStatus(Status.Ongoing);
            this.studentAcademicsServiceImpl.updateStudentAcademics(academics);
            byte[] pdfBytes = PdfGenerator.generateReceiptPdf(this.studentServiceImpl.getStudentById(studentId),
                    receiptMapper.toReceiptResponse(receipt), paymentDetail2);
            DataResponse response = DataResponse.builder()
                    .data(pdfBytes)
                    .message("payment detail added Successfully")
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
            log.info("Successfully added payment detail for studentId: {} and academicId: {}", studentId, academicId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error adding payment detail for studentId: {} and academicId: {}: {}", studentId, academicId,
                    e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    // Get API's ***************************

    @GetMapping("/students/{id}")
    @Operation(summary = "Get student by ID", description = "Fetches details of a specific student by their ID")
    public ResponseEntity<?> studentById(@PathVariable("id") String id) throws Exception {
        log.info("Starting studentById method with studentId: {}", id);
        log.info("Student Detail Fetching for Id : {}", id);
        try {
            StudentDetailResponse student1 = this.studentMapper
                    .toStudentDetailResponse(this.studentServiceImpl.getStudentById(id));
            List<StudentAcademics> academics = this.studentAcademicsServiceImpl.getAcademicsByStudent(id);
            student1.setStudentAcademics(
                    academics == null ? null : this.studentAcademicsMapper.toStudentAcademicsResponseList(academics));
            GuardianInfo guardianInfo = this.guardianInfoServiceImpl.getGuardianInfoByStudent(id);
            student1.setGuardianInfo(
                    guardianInfo == null ? null : this.guardianInfoMapper.toGuardianInfoResponse(guardianInfo));
            BankDetail bankDetail = this.bankDetailServiceImpl.getBankDetailByStudent(id);
            student1.setBankDetail(bankDetail == null ? null : this.bankDetailMapper.toBankDetailResponse(bankDetail));
            LastSchool lastSchool = this.lastSchoolServiceImpl.getLastSchoolByStudent(id);
            student1.setLastSchool(lastSchool == null ? null : this.lastSchoolMapper.toLastSchoolResponse(lastSchool));
            List<DocumentReponse> documents = this.documentServiceImpl.getStudentDocuments(id);
            student1.setDocuments(documents);

            DataResponse response = DataResponse.builder()
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("Get All Users Successfully !")
                    .data(student1)
                    .build();
            log.info("Student Detail Fetched for Id : {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    // Put API's *************************************

    @PutMapping("/students/{id}")
    @Operation(summary = "Update student details", description = "Updates the details of a specific student by their ID")
    public ResponseEntity<?> updateStudent(@PathVariable("id") String id, @RequestBody StudentRequest request)
            throws Exception {
        log.info("Starting updateStudent method with studentId: {}", id);
        log.info("Updating Student Detail for Student ID : {}", id);
        try {
            Student student = this.studentServiceImpl.findById(id);
            if (student == null) {
                log.warn("student not found with id : {}", id);
                throw new EntityNotFoundException("Student not present !");
            }
            student.setFirstName(request.getFirstName());
            student.setFatherName(request.getFatherName());
            student.setMotherName(request.getMotherName());
            student.setSurname(request.getSurname());
            student.setEmail(request.getEmail());
            student.setPhoneNo(request.getPhoneNo());
            student.setDateOfBirth(request.getDateOfBirth());
            student.setGender(request.getGender());
            student.setAdharNo(request.getAdharNo());
            student.setBloodGroup(request.getBloodGroup());
            student.setCaste(request.getCaste());
            student.setCategory(request.getCategory());
            student.setScholarshipCategory(request.getScholarshipCategory());
            student.setLocalAddress(request.getLocalAddress());
            student.setPermanentAddress(request.getPermanentAddress());
            student.setUpdatedDate(LocalDateTime.now());
            this.studentServiceImpl.updateStudent(student);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Student detail updated Successfully !");
            log.info("Updated Student Detail for Student ID : {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/bank-detail/{bkId}")
    @Operation(summary = "Update bank details", description = "Updates the bank details of a specific student by their ID and bank detail ID")
    public ResponseEntity<?> updateBankDetail(@PathVariable("studentId") String studentId,
            @PathVariable("bkId") String bkId,
            @RequestBody BankDetailRequest bankDetail) throws Exception {
        log.info("Starting updateBankDetail method with studentId: {} and bankDetailId: {}", studentId, bkId);
        log.info("Updating Student bank Detail for Student ID : {}", studentId);
        try {
            Student student = this.studentServiceImpl.getStudentById(studentId);
            if (student == null) {
                log.warn("student not found with id : {}", studentId);
                throw new EntityNotFoundException("Student not present !");
            }
            this.bankDetailServiceImpl.updateBankDetail(bankDetail, bkId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "bank detail updated Successfully !");
            log.info("Updated Student bank Detail for Student ID : {}", studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/last-school/{lsId}")
    @Operation(summary = "Update last school details", description = "Updates the last school details of a specific student by their ID and last school ID")
    public ResponseEntity<?> updateLastSchool(@PathVariable("studentId") String studentId,
            @PathVariable("lsId") String lsId,
            @RequestBody LastSchoolRequest lastSchool) throws Exception {
        log.info("Starting updateLastSchool method with studentId: {} and lastSchoolId: {}", studentId, lsId);
        log.info("Updating Student Last School Detail for Student Id :{}", studentId);
        try {
            Student student = this.studentServiceImpl.getStudentById(studentId);
            if (student == null) {
                log.warn("student not found with id : {}", studentId);
                throw new EntityNotFoundException("Student not present !");
            }
            this.lastSchoolServiceImpl.updateLastSchool(lastSchool, lsId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200,
                    "Last School Detail updated Successfully !");
            log.info("Updated Student Last School Detail for Student Id :{}", studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/guardian-info/{giId}")
    @Operation(summary = "Update guardian info", description = "Updates the guardian information of a specific student by their ID and guardian info ID")
    public ResponseEntity<?> updateGuardianInfo(@PathVariable("studentId") String studentId,
            @PathVariable("giId") String giId,
            @RequestBody GuardianInfoRequest guardianInfo) throws Exception {
        log.info("Starting updateGuardianInfo method with studentId: {} and guardianInfoId: {}", studentId, giId);
        log.info("Updating Student Guardian Info for ID : {}", studentId);
        try {
            Student student = this.studentServiceImpl.getStudentById(studentId);
            if (student == null) {
                log.warn("student not found with id : {}", studentId);
                throw new EntityNotFoundException("Student not present !");
            }
            this.guardianInfoServiceImpl.updateGuardianInfo(guardianInfo, giId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200,
                    "Guardian information updated Successfully !");
            log.info("Updated Student Guardian Info for ID : {}", studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/image")
    @Operation(summary = "Update student image", description = "Updates the image of a specific student by their ID")
    public ResponseEntity<?> updateImage(@PathVariable("studentId") String studentId,
            @RequestPart("image") MultipartFile image) throws Exception {
        log.info("Starting updateImage method with studentId: {}", studentId);
        log.info("Updating Student Image for ID : {}", studentId);
        try {
            Student student = this.studentServiceImpl.findById(studentId);
            if (student == null) {
                log.warn("student not found with id : {}", studentId);
                throw new EntityNotFoundException("Student not present !");
            }
            student.setImage(image.getBytes());
            this.studentServiceImpl.updateStudent(student);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Image updated Successfully !");
            log.info("Updated Student Image for ID : {}", studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/academics/{academicsId}")
    @Operation(summary = "Update student academics", description = "Updates the academic details of a specific student by their ID and academic record ID")
    public ResponseEntity<?> updateAcademics(@PathVariable("studentId") String studentId,
            @PathVariable("academicsId") String academicsId,
            @RequestBody UpdateAcademicsRequest request) throws Exception {
        log.info("Starting updateAcademics method with studentId: {} and academicsId: {}", studentId, academicsId);
        log.info("Updating Student Academics for ID : {}", studentId);
        try {

            Student student = this.studentServiceImpl.getStudentById(studentId);
            if (student == null) {
                log.warn("student not found with id : {}", studentId);
                throw new EntityNotFoundException("Student not present !");
            }
            StudentAcademics studentAcademics = this.studentAcademicsServiceImpl.getAcademicsById(academicsId);
            if (studentAcademics == null) {
                log.warn("student Academics not found with id : {}", academicsId);
                throw new EntityNotFoundException("Student Academics not present !");
            }
            studentAcademics.setCollegeName(request.getCollegeName());
            studentAcademics.setStdClass(request.getStdClass());
            studentAcademics.setResult(request.getResult());
            studentAcademics.setMarksObtained(request.getMarksObtained());
            studentAcademics.setExamination(request.getExamination());
            studentAcademics.setExamMonth(request.getExamMonth());
            studentAcademics.setRollNo(request.getRollNo());
            studentAcademics.setStatus(request.getStatus());
            studentAcademics.setSection(request.getSection());
            studentAcademics.setAlumni(request.isAlumni());

            this.studentAcademicsServiceImpl.updateStudentAcademics(studentAcademics);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Academics updated Successfully !");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/academics/{academicsId}/stream/{stId}")
    @Operation(summary = "Update student stream", description = "Updates the stream details of a specific student by their ID and stream ID")
    public ResponseEntity<?> updateStream(@PathVariable("academicsId") String academicsId,
            @PathVariable("stId") long stId,
            @RequestBody StreamRequest stream) throws Exception {
        log.info("Starting updateStream method with academicsId: {} and streamId: {}", academicsId, stId);
        log.info("Updating Student Stream for ID : {}", academicsId);
        try {
            StudentAcademics studentAcademics = this.studentAcademicsServiceImpl.getAcademicsById(academicsId);
            if (studentAcademics == null) {
                log.warn("student Academics not found with id : {}", academicsId);
                throw new EntityNotFoundException("Student Academics not present !");
            }
            Stream stream1 = this.streamServiceImpl.getStreamById(stId);
            if (stream1 == null) {
                log.warn("student Stream not found with id : {}", stId);
                throw new EntityNotFoundException("Student Stream not present !");
            }
            this.streamServiceImpl.deleteStreamById(stId);
            stream1.setMedium(stream.getMedium());
            stream1.setStream(stream.getStream());
            stream1.setSubStream(stream.getSubStream());
            for (String subject : stream.getSubjects()) {
                Subject sb = new Subject();
                sb.setName(subject);
                sb.setStream(stream1);
                this.subjectServiceImpl.addSubject(sb);
            }
            stream1.setAcademics(studentAcademics);
            this.streamServiceImpl.addStream(stream1);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Stream updated Successfully !");
            log.info("Updated Student Stream for ID : {}", academicsId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/academics/{academicsId}/bio-focal-subject/{bfId}")
    @Operation(summary = "Update student bio-focal subject", description = "Updates the bio-focal subject details of a specific student by their ID and bio-focal subject ID")
    public ResponseEntity<?> updateBioFocalSubject(@PathVariable("academicsId") String academicsId,
            @PathVariable("bfId") long bfId,
            @RequestBody BioFocalSubjectRequest bioFocalSubject) throws Exception {
        log.info("Starting updateBioFocalSubject method with academicsId: {} and bioFocalSubjectId: {}", academicsId,
                bfId);
        log.info("Updating Student Bio Focal Subject for ID : {}", academicsId);
        try {
            StudentAcademics studentAcademics = this.studentAcademicsServiceImpl.getAcademicsById(academicsId);
            if (studentAcademics == null) {
                log.warn("student Academics not found with id : {}", academicsId);
                throw new EntityNotFoundException("Student Academics not present !");
            }
            BiofocalSubject biofocalSubject = this.bioFocalSubjectServiceImpl.getBiofocalSubjectById(bfId);
            if (biofocalSubject == null) {
                log.warn("student Bio Focal Subject not found with id : {}", bfId);
                throw new EntityNotFoundException("Student Bio Focal Subject not present !");
            }
            this.bioFocalSubjectServiceImpl.deleteBiofocalSubjectById(bfId);
            biofocalSubject.setMedium(bioFocalSubject.getMedium());
            biofocalSubject.setSubStream(bioFocalSubject.getSubStream());
            biofocalSubject.setAcademics(studentAcademics);
            biofocalSubject.setSubject(bioFocalSubject.getSubject());
            biofocalSubject.setAcademics(studentAcademics);
            this.bioFocalSubjectServiceImpl.addBiofocalSubject(biofocalSubject);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200,
                    "Bio Focal Subject updated Successfully !");
            log.info("Updated Student Bio Focal Subject for ID : {}", academicsId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    // Delete API's ********************************

    @DeleteMapping("/students/{id}")
    @Operation(summary = "Delete student", description = "Deletes a specific student by their ID")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") String id) throws Exception {
        log.info("Starting deleteStudent method with studentId: {}", id);
        try {
            this.studentServiceImpl.deleteStudent(id);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Student deleted Successfully !");
            log.info("Successfully deleted student with ID: {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting student with ID: {}: {}", id, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/bank-detail/{bkId}")
    @Operation(summary = "Delete bank detail", description = "Deletes the bank detail of a specific student by the bank detail ID")
    public ResponseEntity<?> deleteBankDetail(@PathVariable("bkId") String bkId) throws Exception {
        log.info("Starting deleteBankDetail method with bankDetailId: {}", bkId);
        try {
            this.bankDetailServiceImpl.deleteBankDetail(bkId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Student deleted Successfully !");
            log.info("Successfully deleted bank detail with ID: {}", bkId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting bank detail with ID: {}: {}", bkId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/guardian-info/{giId}")
    @Operation(summary = "Delete guardian info", description = "Deletes the guardian information of a specific student by the guardian info ID")
    public ResponseEntity<?> deleteGuardianInfo(@PathVariable("giId") String giId) throws Exception {
        log.info("Starting deleteGuardianInfo method with guardianInfoId: {}", giId);
        try {
            this.guardianInfoServiceImpl.deleteGuardianInfo(giId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Student deleted Successfully !");
            log.info("Successfully deleted guardian info with ID: {}", giId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting guardian info with ID: {}: {}", giId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/last-school/{lsId}")
    @Operation(summary = "Delete last school detail", description = "Deletes the last school detail of a specific student by the last school ID")
    public ResponseEntity<?> deleteLastSchool(@PathVariable("lsId") String lsId) throws Exception {
        log.info("Starting deleteLastSchool method with lastSchoolId: {}", lsId);
        try {
            this.lastSchoolServiceImpl.deleteLastSchool(lsId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Student deleted Successfully !");
            log.info("Successfully deleted last school with ID: {}", lsId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting last school with ID: {}: {}", lsId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/documents/{docId}")
    @Operation(summary = "Update student document", description = "Updates a specific document of a student by their ID and document ID")
    public ResponseEntity<?> updateDocument(@PathVariable("docId") long docId,
            @RequestPart("document") MultipartFile document) throws Exception {
        log.info("Starting updateDocument method with documentId: {}", docId);
        try {
            this.documentService.updateDocument(document.getBytes(), docId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Document updated Successfully !");
            log.info("Successfully updated document");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error updating document: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    // import export

    @PostMapping("/students/upload")
    @Operation(summary = "Import students from CSV", description = "Imports student data from a CSV file")
    public ResponseEntity<?> importStudents(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("Starting importStudents method with file: {}", file.getOriginalFilename());
        String filename = file.getOriginalFilename();
        if (file.isEmpty()) {
            throw new Exception("Please upload a file !");
        }
        if (filename == null ||
                (!filename.endsWith(".csv") &&
                        !filename.endsWith(".xlsx") &&
                        !filename.endsWith(".xls") &&
                        !filename.endsWith(".xlsm"))) {
            throw new Exception("Invalid file format. Please upload a CSV or Excel file (.csv, .xlsx, .xls, .xlsm).");
        }
        try {
            if (filename.endsWith(".csv")) {
                this.studentServiceImpl.saveStudentFromCSV(file);
            } else if (filename.endsWith(".xlsx") || filename.endsWith(".xls") || filename.endsWith(".xlsm")) {
                this.studentServiceImpl.saveStudentFromExcel(file);
            }
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Student data imported successfully !");
            log.info("Successfully imported student data from CSV file: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error importing student data from CSV: {}", e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/students/csv")
    @Operation(summary = "Export students to CSV", description = "Exports student data to a CSV file")
    public ResponseEntity<?> exportStudents(@RequestParam Map<String, String> params) throws Exception {
        log.info("Starting exportStudents method with params: {}", params);
        try {
            ByteArrayInputStream stream = this.studentServiceImpl.loadStudentDataToCSV(params.get("query"),
                    params.get("stdClass"), params.get("section"), params.get("session"));
            if (stream == null) {
                log.warn("No data found for the given parameters: {}", params);
                throw new EntityNotFoundException("No data found for the given parameters!");
            }

            InputStreamResource resource = new InputStreamResource(stream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student.csv");
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            DataResponse response = DataResponse.builder()
                    .data(resource)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("Student data exported successfully !")
                    .build();
            log.info("Successfully exported student data to CSV file");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resource);
        } catch (Exception e) {
            log.error("Error exporting student data to CSV: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/students/excel")
    public ResponseEntity<?> exportStudentsToExcel(@RequestParam Map<String, String> params) throws Exception {
        log.info("Starting exportStudentsToExcel method with params: {}", params);
        try {
            ByteArrayInputStream stream = this.excelService.generateExcel(params.get("query"),
                    params.get("stdClass"), params.get("section"), params.get("session"));
            if (stream == null) {
                log.warn("No data found for the given parameters: {}", params);
                throw new EntityNotFoundException("No data found for the given parameters!");
            }

            InputStreamResource resource = new InputStreamResource(stream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student.xlsx");
            headers.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            log.info("Successfully exported student data to Excel file");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resource);
        } catch (Exception e) {
            log.error("Error exporting student data to Excel: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/students/excel/sample")
    public ResponseEntity<?> sampleSheet() throws Exception {
        try {
            ByteArrayInputStream stream = this.excelService.generateRawExcel();
            if (stream == null) {
                log.warn("No data found");
                throw new EntityNotFoundException("No data found!");
            }

            InputStreamResource resource = new InputStreamResource(stream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student.xlsx");
            headers.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            log.info("Successfully exported Excel file");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resource);
        } catch (Exception e) {
            log.error("Error exporting : {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/delete")
    @Operation(summary = "Delete students by IDs", description = "Deletes multiple students by their IDs")
    public ResponseEntity<?> deleteStudents(@RequestBody List<String> studentIds) throws Exception {
        log.info("Starting deleteStudents method with studentIds: {}", studentIds);
        try {
            for (String studentId : studentIds) {
                this.studentServiceImpl.deleteStudent(studentId);
            }
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Students deleted Successfully !");
            log.info("Successfully deleted students with IDs: {}", studentIds);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting students with IDs: {}: {}", studentIds, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}
