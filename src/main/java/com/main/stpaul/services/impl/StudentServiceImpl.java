package com.main.stpaul.services.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.main.stpaul.constants.Result;
import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.request.ExcelStudent;
import com.main.stpaul.dto.response.PendingStudents;
import com.main.stpaul.entities.AdmissionForm;
import com.main.stpaul.entities.BankDetail;
import com.main.stpaul.entities.GuardianInfo;
import com.main.stpaul.entities.LastSchool;
import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.repository.StudentRepo;
import com.main.stpaul.services.serviceInterface.StudentService;


@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private GuardianInfoServiceImpl guardianInfoServiceImpl;

    @Autowired
    private BankDetailServiceImpl bankDetailServiceImpl;

    @Autowired
    private LastSchoolServiceImpl lastSchoolServiceImpl;

    @Autowired
    private StudentAcademicsServiceImpl academicsServiceImpl;

    @Autowired
    private AdmissionFormServiceImpl admissionFormServiceImpl;

    @Override
    public Student addStudent(Student student) {
        String id = UUID.randomUUID().toString();
        student.setStudentId(id);
        return this.studentRepo.save(student);
    }

    @Override
    public Student getStudentById(String id) {
        Student response = this.studentRepo.findByStudentId(id).orElse(null);
        return response;
    }

    @Override
    public List<Student> getAllStudents(String query,String stdClass,String section,String session) {
        return this.studentRepo.findAllStudents(query,stdClass,section,session);
    }

    @Override
    public Student updateStudent(Student student) {
        return this.studentRepo.save(student);
    }

    @Override
    public void deleteStudent(String id) {
        this.studentRepo.deleteStudent(id);
        return;
    }

    @Override
    public Student findById(String id) {
        return this.studentRepo.findById(id).get();
    }

    @Override
    public List<PendingStudents> getPendingStudents(String query,String stdClass,String section,String session) {
        return this.studentRepo.findByStatus(query,stdClass,section,session,Status.Pending);
    }

    @Override
    public List<Student> getFailStudents(String query,String stdClass,String section,String session) {
        return this.studentRepo.findFailStudents(query,stdClass,section,session,Result.FAIL);
    }

 /**
     * Loads student data from a CSV file and saves it to the database.
     * @param file The CSV file containing student data.
     * @return A Result object indicating the success or failure of the operation.
 * @throws Exception 
     */

    @Override
    public void saveStudentFromCSV(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("File is empty. Please upload a valid CSV file.");
        }
        try {
            List<Student> students = parseCsv(file);
            for (Student student : students) {
                String id = UUID.randomUUID().toString();
                student.setStudentId(id);
                this.studentRepo.save(student);
            }
        } catch (Exception e) {
            throw new Exception("Error while saving student data from CSV: " + e.getMessage());
        }
        return;
    }

    @Override
    public void saveStudentFromExcel(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("File is empty. Please upload a valid Excel file.");
        }
        try {
            List<List<Object>> studentDataList = parseXlsx(file);
            for (List<Object> data : studentDataList) {
                Student student = new Student();
                GuardianInfo guardianInfo = new GuardianInfo();
                // BankDetail bankDetail = new BankDetail();
                LastSchool lastSchool = new LastSchool();
                String formNo = null;
    
                for (Object obj : data) {
                    if (obj instanceof Student) {
                        student = (Student) obj;
                    } else if (obj instanceof GuardianInfo) {
                        guardianInfo = (GuardianInfo) obj;
                    // } else if (obj instanceof BankDetail) {
                    //     bankDetail = (BankDetail) obj;
                    // }
                    } else if (obj instanceof LastSchool) {
                        lastSchool = (LastSchool) obj;
                    }else if(obj instanceof String) {
                        formNo = (String) obj;
                    }
                }
    
                if (student != null) {
                    student = addStudent(student);
                    guardianInfo.setStudent(student);
                    this.guardianInfoServiceImpl.addGuardianInfo(guardianInfo);
                    // bankDetail.setStudent(student);
                    // this.bankDetailServiceImpl.addBankDetail(bankDetail);
                    lastSchool.setStudent(student);
                    this.lastSchoolServiceImpl.addLastSchool(lastSchool);
                    StudentAcademics studentAcademics = new StudentAcademics();
                    studentAcademics.setStudent(student);
                    studentAcademics.setStatus(Status.Pending);
                    studentAcademics.setStdClass(student.getStdClass());
                    studentAcademics.setSection(student.getSection());
                    studentAcademics.setSession(student.getSession());
                    studentAcademics.setRollNo(student.getRollNo());
                    this.academicsServiceImpl.addStudentAcademics(studentAcademics);
                    AdmissionForm admissionForm = new AdmissionForm();
                    admissionForm.setStudent(student);
                    admissionForm.setStdClass(student.getStdClass());
                    admissionForm.setAdmissionDate(String.valueOf(student.getAdmissionDate()));
                    admissionForm.setSession(student.getSession());
                    admissionForm.setFormNo(formNo);
                    this.admissionFormServiceImpl.addAdmissionForm(admissionForm);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error while saving student data from Excel: " + e.getMessage(), e);
        }
        return;
    }


    @Override
    public ByteArrayInputStream loadStudentDataToCSV(String query, String stdClass, String section, String session) {
        List<Student> students = this.studentRepo.findAllStudents(query,stdClass,section,session);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try(CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), CSVFormat.DEFAULT.withHeader("studentId", "name", "fatherName", "motherName", "surname", "section",
                 "session","gender","stdClass","adharNo","phoneNo","email","dateOfBirth"))) {
            for (Student student : students) {
                csvPrinter.printRecord(student.getStudentId(), student.getFirstName(), 
                student.getFatherName(), student.getMotherName(), student.getSurname(), student.getSection(), student.getSession(),student.getGender(), student.getStdClass(), student.getAdharNo(), student.getPhoneNo(), student.getEmail(), student.getDateOfBirth());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());

    }

    /**
     * Parses a CSV file and converts it into a list of Student objects.
     * @param file The CSV file to parse.
     * @return A list of Student objects.
     * @throws Exception If an error occurs while parsing the file.
     */

    private List<Student> parseCsv(MultipartFile file) throws Exception{
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(),StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : csvParser) {
                Student student = new Student();
                student.setFirstName(csvRecord.get("name"));
                student.setFatherName(csvRecord.get("fatherName"));
                student.setMotherName(csvRecord.get("motherName"));
                student.setSurname(csvRecord.get("surname"));
                student.setSection(csvRecord.get("section"));
                student.setSession(csvRecord.get("session"));
                student.setStdClass(csvRecord.get("stdClass"));
                student.setEmail(csvRecord.get("email"));
                student.setPhoneNo(csvRecord.get("phoneNo"));
                student.setAdharNo(csvRecord.get("adharNo"));
                student.setGender(csvRecord.get("gender"));
                student.setDateOfBirth(LocalDate.parse(csvRecord.get("dateOfBirth")));

                // Add other fields as necessary
                students.add(student);
            }
            return students;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<List<Object>> parseXlsx(MultipartFile file) throws Exception {
    List<List<Object>> students = new ArrayList<>();

    try (InputStream inputStream = file.getInputStream();
         Workbook workbook = WorkbookFactory.create(inputStream)) {

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        if (!rowIterator.hasNext()) return students;

        Row headerRow = rowIterator.next();

        // Extract all indexes at once
        Map<String, Integer> indexMap = new HashMap<>();
        String[] headers = {"formNo",
            "firstName", "fatherName", "motherName", "surname","email","phoneNo",
            "dateOfBirth", "gender", "aadharNo", "bloodGroup", "caste", "category", "scholarship",
            "admissionDate", "section", "session", "stdClass", "rollNo",
            "guardianName", "guardianRelation", "guardianOccupation", "guardianIncome","guardianPhoneNo",
            "lastSchoolName", "lastStudentId", "lastRollNo", "uId", "lastExamination", "examMonth", "marksObtained", "result"
        };

        for (String header : headers) {
            int idx = getColumnIndex(headerRow, header);
            if (idx == -1) {
                throw new RuntimeException("Missing column: " + header);
            }
            indexMap.put(header, idx);
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            List<Object> objects = new ArrayList<>();

            // Student
            Student student = new Student();
            student.setFirstName(getCellValue(row.getCell(indexMap.get("firstName"))));
            student.setFatherName(getCellValue(row.getCell(indexMap.get("fatherName"))));
            student.setMotherName(getCellValue(row.getCell(indexMap.get("motherName"))));
            student.setSurname(getCellValue(row.getCell(indexMap.get("surname"))));
            student.setEmail(getCellValue(row.getCell(indexMap.get("email"))));
            student.setPhoneNo(getCellValue(row.getCell(indexMap.get("phoneNo"))));
            student.setDateOfBirth(parseLocalDate(row.getCell(indexMap.get("dateOfBirth"))));
            student.setGender(getCellValue(row.getCell(indexMap.get("gender"))));
            student.setAdharNo(String.valueOf(getCellValue(row.getCell(indexMap.get("aadharNo")))));
            student.setBloodGroup(getCellValue(row.getCell(indexMap.get("bloodGroup"))));
            student.setCaste(getCellValue(row.getCell(indexMap.get("caste"))));
            student.setCategory(getCellValue(row.getCell(indexMap.get("category"))));
            student.setScholarshipCategory(getCellValue(row.getCell(indexMap.get("scholarship"))));
            student.setAdmissionDate(parseLocalDate(row.getCell(indexMap.get("admissionDate"))));
            student.setSection(getCellValue(row.getCell(indexMap.get("section"))));
            student.setSession(getCellValue(row.getCell(indexMap.get("session"))));
            student.setStdClass(getCellValue(row.getCell(indexMap.get("stdClass"))));
            student.setRollNo(getCellValue(row.getCell(indexMap.get("rollNo"))));

            // GuardianInfo
            GuardianInfo guardianInfo = new GuardianInfo();
            guardianInfo.setName(getCellValue(row.getCell(indexMap.get("guardianName"))));
            guardianInfo.setPhone(getCellValue(row.getCell(indexMap.get("guardianPhoneNo"))));
            guardianInfo.setRelation(getCellValue(row.getCell(indexMap.get("guardianRelation"))));
            guardianInfo.setOccupation(getCellValue(row.getCell(indexMap.get("guardianOccupation"))));
            guardianInfo.setIncome(safeParseDouble(getCellValue(row.getCell(indexMap.get("guardianIncome")))));

            // LastSchool
            LastSchool lastSchool = new LastSchool();
            lastSchool.setCollegeName(getCellValue(row.getCell(indexMap.get("lastSchoolName"))));
            lastSchool.setLastStudentId(getCellValue(row.getCell(indexMap.get("lastStudentId"))));
            lastSchool.setRollNo(getCellValue(row.getCell(indexMap.get("lastRollNo"))));
            lastSchool.setUid(getCellValue(row.getCell(indexMap.get("uId"))));
            lastSchool.setExamination(getCellValue(row.getCell(indexMap.get("lastExamination"))));
            lastSchool.setExamMonth(getCellValue(row.getCell(indexMap.get("examMonth"))));
            lastSchool.setMarksObtained(safeParseDouble(getCellValue(row.getCell(indexMap.get("marksObtained")))));
            lastSchool.setResult(getCellValue(row.getCell(indexMap.get("result"))));

            objects.add(student);
            objects.add(guardianInfo);
            objects.add(lastSchool);
            objects.add(getCellValue(row.getCell(indexMap.get("formNo"))));
            students.add(objects);
        }
    }

    return students;
}

    

// Get the column index by header name
private int getColumnIndex(Row headerRow, String columnName) {
    for (Cell cell : headerRow) {
        if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
            return cell.getColumnIndex();
        }
    }
    return -1; // Return -1 if not found
}

// Get cell value as string
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    private LocalDate parseLocalDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            return LocalDate.parse(getCellValue(cell));
        } catch (Exception e) {
            return null;
        }
    }
    
    private Double safeParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

}
