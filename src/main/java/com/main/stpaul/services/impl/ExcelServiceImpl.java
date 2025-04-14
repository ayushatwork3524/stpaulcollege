package com.main.stpaul.services.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.entities.Subject;
import com.main.stpaul.services.serviceInterface.ExcelService;
import com.main.stpaul.services.serviceInterface.StudentService;

@Service
public class ExcelServiceImpl implements ExcelService {

    private StudentService studentService;

    public ExcelServiceImpl(StudentService studentService) {
        this.studentService = studentService;
    }

    // main method to generate excel file
    @Override
    public ByteArrayInputStream generateExcel(String query, String stdClass, String section, String session)
            throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            List<Student> students = this.studentService.getAllStudents(query, stdClass, section, session);

            generateStudentSheet(workbook, students);

            generateGuardianSheet(workbook, students);

            // generateBankSheet(workbook, students);

            generateLastSheet(workbook, students);

            generateAcademicSheet(workbook, students);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to generate Excel file", e);
        }
    }

    // method to generate student sheet of excel file
    private void generateStudentSheet(XSSFWorkbook workbook, List<Student> students) {

        XSSFSheet sheet = workbook.createSheet("Student Details");
        XSSFRow headerRow = sheet.createRow(0);
        int dataIndex = 1;

        List<String> headers = List.of("Form Number", "Admission Date", "Admission Number", "Session", "Section",
                "Class",
                "First Name", "Father Name",
                "Last Name",
                "Mother Name", "Email",
                "Phone Number", "DOB",
                "Gender", "Adhar Number", "Blood Group", "Caste", "Category", "Scholarship Category");
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }

        generateStudentEntries(dataIndex, sheet, students);
    }

    // method to generate guardian sheet of excel file
    private void generateGuardianSheet(XSSFWorkbook workbook, List<Student> students) {

        XSSFSheet sheet = workbook.createSheet("Guardian Details");
        XSSFRow headerRow = sheet.createRow(0);

        int dataIndex = 1;

        List<String> headers = List.of("Student Name", "Guardian Name", "Guardian Phone Number", "Guardian Occupation",
                "Guardian Relation", "Guardian Income");

        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }

        generateGuardianEntries(dataIndex, sheet, students);
    }

    // method to generate bank sheet of excel file
    private void generateBankSheet(XSSFWorkbook workbook, List<Student> students) {

        XSSFSheet sheet = workbook.createSheet("Bank Details");
        XSSFRow headerRow = sheet.createRow(0);

        int dataIndex = 1;

        List<String> headers = List.of("Student Name", "Bank Name", "Branch", "Account Number",
                "IFSC");

        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }

        generateBankEntries(dataIndex, sheet, students);
    }

    // method to generate last school sheet of excel file
    private void generateLastSheet(XSSFWorkbook workbook, List<Student> students) {

        XSSFSheet sheet = workbook.createSheet("Last School Details");
        XSSFRow headerRow = sheet.createRow(0);

        int dataIndex = 1;

        List<String> headers = List.of("Student Name", "College Name", "Last Student ID", "Roll Number",
                "UID", "Examination", "Exam Month", "Marks Obtained", "Result");

        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }

        generateLastEntries(dataIndex, sheet, students);
    }

    // method to generate academic detail sheet of excel file
    private void generateAcademicSheet(XSSFWorkbook workbook, List<Student> students) {

        XSSFSheet sheet = workbook.createSheet("Academics Details");
        XSSFRow headerRow = sheet.createRow(0);

        int dataIndex = 1;

        List<String> headers = List.of("Student Name", "Role Number", "Examination", "Exam Month",
                "Marks", "Class", "Session", "Section", "Result", "Alumni", "Status", "Stream", "Sub Stream", "Medium",
                "Subjects", "Bio Focal Subject");

        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }

        generateAcademicEntries(dataIndex, sheet, students);
    }

    // method to generate student entries in the first sheet of excel file
    private void generateStudentEntries(int dataIndex, XSSFSheet sheet, List<Student> students) {

        for (Student student : students) {
            XSSFRow row = sheet.createRow(dataIndex);

            if (student.getAdmissionForm() != null) {

                row.createCell(0).setCellValue(student.getAdmissionForm().getFormNo());
                row.createCell(1).setCellValue(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd").format(student.getAdmissionForm().getAddedDate()));
                row.createCell(2).setCellValue(student.getAdmissionForm().getFormNo());
            }
            row.createCell(3).setCellValue(student.getSession());
            row.createCell(4).setCellValue(student.getSection());
            row.createCell(5).setCellValue(student.getStdClass());
            row.createCell(6).setCellValue(student.getFirstName());
            row.createCell(7).setCellValue(student.getFatherName());
            row.createCell(8).setCellValue(student.getSurname());
            row.createCell(9).setCellValue(student.getMotherName());
            row.createCell(10).setCellValue(student.getEmail());
            row.createCell(11).setCellValue(student.getPhoneNo());
            row.createCell(12).setCellValue(student.getDateOfBirth());
            row.createCell(13).setCellValue(student.getGender());
            row.createCell(14).setCellValue(student.getAdharNo());
            row.createCell(15).setCellValue(student.getBloodGroup());
            row.createCell(16).setCellValue(student.getCaste());
            row.createCell(17).setCellValue(student.getCategory());
            row.createCell(18).setCellValue(student.getScholarshipCategory());
            dataIndex++;
        }
        ;
        return;

    }

    // method to generate guardian entries in the second sheet of excel file
    private void generateGuardianEntries(int dataIndex, XSSFSheet sheet,
            List<Student> students) {

        for (Student student : students) {

            XSSFRow row = sheet.createRow(dataIndex);

            row.createCell(0)
                    .setCellValue(student.getFirstName() + " " + student.getFatherName() + " " + student.getSurname());
            row.createCell(1).setCellValue(student.getGuardianInfo().getName());
            row.createCell(2).setCellValue(student.getGuardianInfo().getPhone());
            row.createCell(3).setCellValue(student.getGuardianInfo().getOccupation());
            row.createCell(4).setCellValue(student.getGuardianInfo().getRelation());
            row.createCell(5).setCellValue(student.getGuardianInfo().getIncome());

            dataIndex++;
        }
        ;
        return;

    }

    // method to generate bank entries in the second sheet of excel file
    private void generateBankEntries(int dataIndex, XSSFSheet sheet,
            List<Student> students) {

        for (Student student : students) {

            XSSFRow row = sheet.createRow(dataIndex);

            row.createCell(0)
                    .setCellValue(student.getFirstName() + " " + student.getFatherName() + " " + student.getSurname());
            row.createCell(1).setCellValue(student.getBankDetail().getBankName());
            row.createCell(2).setCellValue(student.getBankDetail().getBranchName());
            row.createCell(3).setCellValue(student.getBankDetail().getAccountNo());
            row.createCell(4).setCellValue(student.getBankDetail().getIfscCode());

            dataIndex++;
        }
        ;
        return;

    }

    // method to generate last school entries in the second sheet of excel file
    private void generateLastEntries(int dataIndex, XSSFSheet sheet,
            List<Student> students) {

        for (Student student : students) {

            XSSFRow row = sheet.createRow(dataIndex);

            row.createCell(0)
                    .setCellValue(student.getFirstName() + " " + student.getFatherName() + " " + student.getSurname());
            if (student.getLastSchool() != null) {

                row.createCell(1).setCellValue(student.getLastSchool().getCollegeName());
                row.createCell(2).setCellValue(student.getLastSchool().getLastStudentId());
                row.createCell(3).setCellValue(student.getLastSchool().getRollNo());
                row.createCell(4).setCellValue(student.getLastSchool().getUid());
                row.createCell(5).setCellValue(student.getLastSchool().getExamination());
                row.createCell(6).setCellValue(student.getLastSchool().getExamMonth());
                row.createCell(7).setCellValue(student.getLastSchool().getMarksObtained());
                row.createCell(8).setCellValue(student.getLastSchool().getResult());

            }
            dataIndex++;
        }
        ;
        return;

    }

    // method to generate academic entries in the second sheet of excel file
    private void generateAcademicEntries(int dataIndex, XSSFSheet sheet, List<Student> students) {
        for (Student student : students) {
            List<StudentAcademics> studentAcademics = student.getStudentAcademics();
            String fullName = student.getFirstName() + " " + student.getFatherName() + " " + student.getSurname();

            for (StudentAcademics academic : studentAcademics) {
                XSSFRow row = sheet.createRow(dataIndex++);

                // Column A - Student Name
                row.createCell(0).setCellValue(fullName);

                // Column B to P - Academic Info
                row.createCell(1).setCellValue(academic.getRollNo());
                row.createCell(2).setCellValue(academic.getExamination());
                row.createCell(3).setCellValue(academic.getExamMonth());
                row.createCell(4).setCellValue(academic.getMarksObtained());
                row.createCell(5).setCellValue(academic.getStdClass());
                row.createCell(6).setCellValue(academic.getSession());
                row.createCell(7).setCellValue(academic.getSection());
                row.createCell(8).setCellValue(academic.getResult().toString());
                row.createCell(9).setCellValue(academic.isAlumni() ? "Yes" : "No");
                row.createCell(10).setCellValue(academic.getStatus().toString());

                if (academic.getStream() != null) {
                    row.createCell(11).setCellValue(academic.getStream().getStream());
                    row.createCell(12).setCellValue(academic.getStream().getSubStream());
                    row.createCell(13).setCellValue(academic.getStream().getMedium());
                    List<String> subjectNames = academic.getStream().getSubjects()
                            .stream()
                            .map(Subject::getName)
                            .collect(Collectors.toList());

                    row.createCell(14).setCellValue(String.join(", ", subjectNames));
                } else {
                    row.createCell(11).setCellValue("-");
                    row.createCell(12).setCellValue("-");
                    row.createCell(13).setCellValue("-");
                    row.createCell(14).setCellValue("-");
                }

                if (academic.getBiofocalSubject() != null) {
                    row.createCell(15).setCellValue(
                            academic.getBiofocalSubject().getSubStream() + " - " +
                                    academic.getBiofocalSubject().getSubject() + " (" +
                                    academic.getBiofocalSubject().getMedium() + ")");
                } else {
                    row.createCell(15).setCellValue("-");
                }
            }
        }
    }

    // method to generate raw data sheet of excel file
    @Override
    public ByteArrayInputStream generateRawExcel() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Raw Data");
            XSSFRow headerRow = sheet.createRow(0);

            List<String> headers = List.of("formNo", "session", "stdClass", "section", "firstName", "fatherName",
                    "surname", "motherName",
                    "rollNo", "email",
                    "phoneNo", "dateOfBirth",
                    "gender", "aadharNo",
                    "bloodGroup", "caste", "category", "scholarship", "admissionDate", "guardianName",
                    "guardianPhoneNo",
                    "guardianRelation", "guardianOccupation",
                    "guardianIncome", "lastSchoolName", "lastStudentId", "lastRollNo", "uId", "lastExamination",
                    "examMonth", "marksObtained", "result");
            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }

            // Sample data for demonstration purposes
            XSSFRow dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("256");
            dataRow.createCell(1).setCellValue("2023-2024");
            dataRow.createCell(2).setCellValue("10");
            dataRow.createCell(3).setCellValue("A");
            dataRow.createCell(4).setCellValue("John");
            dataRow.createCell(5).setCellValue("Doe");
            dataRow.createCell(6).setCellValue("Smith");
            dataRow.createCell(7).setCellValue("Jane");
            dataRow.createCell(8).setCellValue("1234");
            dataRow.createCell(9).setCellValue("john@gmail.com");
            dataRow.createCell(10).setCellValue("9876543210");
            dataRow.createCell(11).setCellValue("2005-01-01");
            dataRow.createCell(12).setCellValue("Male");
            dataRow.createCell(13).setCellValue("123456789012");
            dataRow.createCell(14).setCellValue("A+");
            dataRow.createCell(15).setCellValue("General");
            dataRow.createCell(16).setCellValue("General");
            dataRow.createCell(17).setCellValue("None");
            dataRow.createCell(18).setCellValue("2023-01-01");
            // dataRow.createCell(17).setCellValue("Bank of America");
            // dataRow.createCell(18).setCellValue("Main Branch");
            // dataRow.createCell(19).setCellValue("1234567890");
            // dataRow.createCell(20).setCellValue("IFSC123");
            dataRow.createCell(19).setCellValue("Michael");
            dataRow.createCell(20).setCellValue("4534354");
            dataRow.createCell(21).setCellValue("Father");
            dataRow.createCell(22).setCellValue("Engineer");
            dataRow.createCell(23).setCellValue("50000");
            dataRow.createCell(24).setCellValue("ABC School");
            dataRow.createCell(25).setCellValue("5678");
            dataRow.createCell(26).setCellValue("91011");
            dataRow.createCell(27).setCellValue("21351331");
            dataRow.createCell(28).setCellValue("MH STATE");
            dataRow.createCell(29).setCellValue("March 2023");
            dataRow.createCell(30).setCellValue("85");
            dataRow.createCell(31).setCellValue("PASS");

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to generate Excel file", e);
        }
    }

}
