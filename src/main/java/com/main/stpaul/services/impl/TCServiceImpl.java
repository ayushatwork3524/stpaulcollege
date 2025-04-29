package com.main.stpaul.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.BorderRadius;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.main.stpaul.dto.request.TransferCertificateRequest;
import com.main.stpaul.entities.Stream;
import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.entities.Subject;
import com.main.stpaul.services.serviceInterface.TCService;

@Service
public class TCServiceImpl implements TCService {

        @Override
        public ByteArrayOutputStream generateTc(Student student, TransferCertificateRequest req) throws IOException {
                try {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PdfWriter writer = new PdfWriter(baos);
                        PdfDocument pdf = new PdfDocument(writer);
                        Document doc = new Document(pdf, PageSize.A4);
                        doc.setMargins(10, 20, 10, 20);

                        InputStream logoStream = new ClassPathResource("logo.png").getInputStream();
                        byte[] logoBytes = logoStream.readAllBytes();
                        Image logo = new Image(ImageDataFactory.create(logoBytes));
                        logo.setHeight(60);
                        logo.setAutoScaleWidth(true);

                        int DOUBLE_SPACE = 14;

                        // Header section
                        Table header = new Table(UnitValue.createPercentArray(new float[] { 1, 4 }))

                                        .useAllAvailableWidth();
                        header.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
                        Cell titleCell = new Cell().add(new Paragraph(
                                        "VIVEKSHIL MITRA PARIWAR INSTITUTION")
                                        .setFontSize(11).setTextAlignment(TextAlignment.CENTER))
                                        .add(new Paragraph(
                                                        "ST. PAUL SENIOR SECONDARY SCHOOL")
                                                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                                                        .setFontSize(20).setTextAlignment(TextAlignment.CENTER))
                                        .add(new Paragraph(
                                                        "Affiliated to Central Board of Secondary Education, New Delhi (Affiliation No: 1130345)\nNear Bridge, Hudkeshwar Road, Nagpur-440034 (Maharashtra)")
                                                        .setFontSize(10).setTextAlignment(TextAlignment.CENTER))
                                        .add(new Paragraph(
                                                        "Email - info@stpaulnagpur.com    Website - www.stpaulschool.ac.in")
                                                        .setFontSize(10).setTextAlignment(TextAlignment.CENTER))
                                        .add(new Paragraph("UDISE CODE :- 27090908705")
                                                        .setFontSize(10)
                                                        .setFont(PdfFontFactory
                                                                        .createFont(StandardFonts.HELVETICA_BOLD))
                                                        .setTextAlignment(TextAlignment.CENTER));
                        header.addCell(titleCell.setBorder(Border.NO_BORDER));
                        header.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.2f));
                        doc.add(header);

                        Div titleDiv = new Div()
                                        .add(new Paragraph("TRANSFER CERTIFICATE")
                                                        .setFontSize(12)
                                                        .setWidth(UnitValue.createPercentValue(30))
                                                        .setPadding(2)
                                                        .setBorderRadius(new BorderRadius(5f))
                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                        .setFont(PdfFontFactory
                                                                        .createFont(StandardFonts.HELVETICA_BOLD))
                                                        .setBorder(new SolidBorder(1.5f)))
                                        .setTextAlignment(TextAlignment.CENTER);

                        doc.add(titleDiv);

                        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

                        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

                        // TC Info
                        Table tcTable = new Table(UnitValue.createPercentArray(new float[] { 4, 4, 4, 4 }))
                                        .useAllAvailableWidth()
                                        .setMarginTop(1)
                                        .setBorder(Border.NO_BORDER)
                                        .setFontSize(9.8f);

                        addSingleRow(tcTable, "TC NO. ", req.getTcNo());

                        if (req.getAdmissionNo() != null && !req.getAdmissionNo().isEmpty()) {
                                addSingleRow(tcTable, "Admission No. ", req.getAdmissionNo());
                        } else if (req.getAdmissionNo() == null) {
                                addSingleRow(tcTable, "Admission No. ", "");
                        } else {
                                addSingleRow(tcTable, "Admission No. ", student.getAdmissionForm().getFormNo());
                        }

                        if (req.getStudentId() != null && !req.getStudentId().isEmpty()) {
                                addSingleRow(tcTable, "Student(Portal) ID No. ", req.getStudentId());
                        } else {
                                addSingleRow(tcTable, "Student(Portal) ID No. ", "");
                        }

                        addSingleRowTwoColumn(tcTable, "UID Adhar Card No. ", student.getAdharNo());

                        doc.add(tcTable);

                        Table bodyTable = new Table(UnitValue.createPercentArray(new float[] { 4, 4 }))
                                        .useAllAvailableWidth()
                                        .setMarginTop(3)
                                        .setBorder(Border.NO_BORDER)
                                        .setFontSize(9.8f);

                        addSingleRowTwoColumn(bodyTable, "1. Name of the pupil ", student.getFullName().toUpperCase());

                        addSingleRowTwoColumn(bodyTable, "2. Father's Name/Guardian's Name  ",
                                        student.getFatherName().toUpperCase() + " "
                                                        + student.getSurname().toUpperCase());

                        addSingleRowTwoColumn(bodyTable, "3. Mother's Name:  ", student.getMotherName().toUpperCase()
                                        + " " + student.getSurname().toUpperCase());

                        doc.add(bodyTable);

                        Table nationalityTable = new Table(
                                        4)
                                        .useAllAvailableWidth()
                                        .setMarginTop(3)
                                        .setBorder(Border.NO_BORDER)
                                        .setFontSize(9.8f);

                        nationalityTable.addCell(new Cell()
                                        .add(new Paragraph("4. Nationality "))
                                        .setWidth(UnitValue.createPercentValue(50))
                                        .setBorder(Border.NO_BORDER));

                        nationalityTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        "INDIAN", DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                        .setFont(("INDIAN" == null || "INDIAN".trim().isEmpty()) ? normal
                                                        : bold));

                        nationalityTable.addCell(new Cell().add(new Paragraph("Mother Tongue "))
                                        .setBorder(Border.NO_BORDER));

                        nationalityTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        "MARATHI", DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                        .setFont(("MARATHI" == null || "MARATHI".trim().isEmpty()) ? normal
                                                        : bold));

                        doc.add(nationalityTable);

                        Table sixColumn = new Table(6)
                                        .useAllAvailableWidth()
                                        .setMarginTop(3)
                                        .setBorder(Border.NO_BORDER)
                                        .setFontSize(9.8f);

                        sixColumn.addCell(new Cell().add(new Paragraph("5. Religion "))
                                        .setBorder(Border.NO_BORDER).setWidth(UnitValue.createPercentValue(50)));

                        if (req.getReligion() == null) {
                                req.setReligion("");
                        } else if (req.getReligion().trim().isEmpty()) {
                                req.setReligion("HINDU");
                        } else {
                                req.setReligion(req.getReligion().trim());
                        }

                        sixColumn.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        req.getReligion().toUpperCase(), DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                        .setFont((req.getReligion() == null || req.getReligion().trim().isEmpty())
                                                        ? normal
                                                        : bold));

                        sixColumn.addCell(new Cell().add(new Paragraph("Category "))
                                        .setBorder(Border.NO_BORDER));

                        if (req.getCategory() == null) {
                                req.setCategory("");
                        }

                        sixColumn.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        req.getCategory().toUpperCase(), 13))).setBorder(Border.NO_BORDER)
                                        .setFont((req.getCategory() == null || req.getCategory().trim().isEmpty())
                                                        ? normal
                                                        : bold));

                        sixColumn.addCell(new Cell().add(new Paragraph("Caste "))
                                        .setBorder(Border.NO_BORDER));

                        sixColumn.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        student.getCaste().toUpperCase(), DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                        .setFont((student.getCaste() == null || student.getCaste().trim().isEmpty())
                                                        ? normal
                                                        : bold));

                        doc.add(sixColumn);

                        Table doaTable = new Table(
                                        UnitValue.createPercentArray(new float[] { 25f, 25f, 16.6f, 16.6f,
                                                        16.6f }))
                                        .useAllAvailableWidth()
                                        .setMarginTop(3)
                                        .setBorder(Border.NO_BORDER)
                                        .setFontSize(9.8f);

                        doaTable.addCell(new Cell(1, 2).add(new Paragraph("6. Date of first admission in the school "))
                                        .setBorder(Border.NO_BORDER));

                        doaTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        convertDate(student.getAdmissionForm()
                                                        .getAdmissionDate()),
                                        DOUBLE_SPACE)))
                                        .setBorder(Border.NO_BORDER)
                                        .setFont((student.getAdmissionForm().getAdmissionDate() == null || student
                                                        .getAdmissionForm().getAdmissionDate().trim().isEmpty())
                                                                        ? normal
                                                                        : bold));

                        doaTable.addCell(new Cell().add(new Paragraph("in"))
                                        .setBorder(Border.NO_BORDER));

                        doaTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        numberToRoman(Integer.parseInt(student.getAdmissionForm()
                                                        .getStdClass())),
                                        DOUBLE_SPACE)))
                                        .setBorder(Border.NO_BORDER)
                                        .setFont((student.getAdmissionForm().getStdClass() == null
                                                        || student.getAdmissionForm().getStdClass().trim().isEmpty())
                                                                        ? normal
                                                                        : bold));

                        doaTable.addCell(new Cell(1, 2)
                                        .add(new Paragraph("7. Date of Birth (In Christian Era) according to the "))
                                        .setBorder(Border.NO_BORDER));

                        doaTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        convertDate(student.getDateOfBirth()
                                                        .toString()),
                                        DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                        .setFont((student.getDateOfBirth().toString() == null
                                                        || student.getDateOfBirth().toString().trim().isEmpty())
                                                                        ? normal
                                                                        : bold));

                        doaTable.addCell(new Cell().add(new Paragraph("Place of Birth "))
                                        .setBorder(Border.NO_BORDER));

                        if (req.getPlace() == null) {
                                req.setPlace("");
                        }

                        doaTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        req.getPlace().toUpperCase(), DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                        .setFont((req.getPlace() == null || req.getPlace().trim().isEmpty()) ? normal
                                                        : bold));

                        doc.add(doaTable);

                        Table dobTable = new Table(UnitValue.createPercentArray(new float[] { 4, 4 }))
                                        .useAllAvailableWidth()
                                        .setMarginTop(3)
                                        .setBorder(Border.NO_BORDER)
                                        .setFontSize(9.8f);

                        dobTable.addCell(new Cell().add(new Paragraph("Admission Register (in figures)\n(in Words)"))
                                        .setBorder(Border.NO_BORDER).setPaddingLeft(18));

                        dobTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        convertDateToWords(convertDate(student.getDateOfBirth().toString())),
                                        DOUBLE_SPACE)))
                                        .setBorder(Border.NO_BORDER)
                                        .setFont((student.getDateOfBirth() == null
                                                        || student.getDateOfBirth().toString().trim()
                                                                        .isEmpty()) ? normal
                                                                                        : bold));

                        doc.add(dobTable);

                        Table studyTable = new Table(
                                        UnitValue.createPercentArray(new float[] { 50, 12.5f, 12.5f, 12.5f, 12.5f }))
                                        .useAllAvailableWidth()
                                        .setMarginTop(3)
                                        .setBorder(Border.NO_BORDER)
                                        .setFontSize(9.8f);

                        studyTable.addCell(new Cell()
                                        .add(new Paragraph("8. Class in which the pupil last studied/is studying"))
                                        .setBorder(Border.NO_BORDER));

                        studyTable.addCell(new Cell()
                                        .add(new Paragraph("(In Figures)"))
                                        .setBorder(Border.NO_BORDER));

                        studyTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        student.getStudentAcademics() == null ? ""
                                                        : numberToRoman(Integer.parseInt(
                                                                        student.getStudentAcademics().get(0)
                                                                                        .getStdClass())),
                                        DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                        .setFont((student.getStudentAcademics() == null) ? normal
                                                        : bold));

                        studyTable.addCell(new Cell()
                                        .add(new Paragraph("(In Words)"))
                                        .setBorder(Border.NO_BORDER));

                        studyTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        student.getStudentAcademics() == null ? ""
                                                        : numberToOrdinal(Integer.parseInt(
                                                                        student.getStudentAcademics().get(0)
                                                                                        .getStdClass())),
                                        DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                        .setFont(("11" == null || "11".trim().isEmpty()) ? normal
                                                        : bold));

                        doc.add(studyTable);

                        Table twoColumnTable = new Table(UnitValue.createPercentArray(new float[] { 4, 4 }))
                                        .useAllAvailableWidth()
                                        .setMarginTop(3)
                                        .setBorder(Border.NO_BORDER)
                                        .setFontSize(9.8f);

                        addSingleRowTwoColumn(twoColumnTable, "9. School/Board Annual Examination last taken ",
                                        student.getLastSchool().getCollegeName());

                        boolean isPromotable = student.getStdClass().equals("12") ? false : true;

                        addSingleRowTwoColumn(twoColumnTable, "10. Weather qualified for Promotion of higher class",
                                        underlineIfEmpty(isPromotable
                                                        ? numberToRoman(Integer.parseInt(student.getStdClass()) + 1)
                                                        : "", DOUBLE_SPACE));

                        twoColumnTable.addCell(new Cell().add(new Paragraph("If so, to which class (in Words)"))
                                        .setBorder(Border.NO_BORDER).setPaddingLeft(18));

                        twoColumnTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                        isPromotable ? numberToOrdinal(Integer.parseInt(student.getStdClass()) + 1)
                                                        : "",
                                        DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                        .setFont(("12" == null || "12".trim().isEmpty()) ? normal
                                                        : bold));

                        List<StudentAcademics> academics = student.getStudentAcademics();
                        String subjectNames = "";

                        if (academics != null && !academics.isEmpty()) {
                                Stream stream = academics.get(0).getStream();
                                if (stream != null && stream.getSubjects() != null) {
                                        subjectNames = stream.getSubjects().stream()
                                                        .filter(Objects::nonNull)
                                                        .map(Subject::getName)
                                                        .filter(Objects::nonNull)
                                                        .collect(Collectors.joining(", "));
                                }
                        }

                        addSingleRowTwoColumn(twoColumnTable, "11. Subject Studied ",
                                        subjectNames);

                        if (req.getDuePaid() == null) {
                                req.setDuePaid("");
                        }

                        addSingleRowTwoColumn(twoColumnTable, "12. Month up to which dues paid ",
                                        req.getDuePaid().toUpperCase());

                        if (req.getFee() == null) {
                                req.setFee("");
                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "13. Any Fee Concession awailed of if so, the nature of: ",
                                        req.getFee().toUpperCase());

                        twoColumnTable.addCell(new Cell().add(new Paragraph("such concession"))
                                        .setBorder(Border.NO_BORDER).setPaddingLeft(18));

                        twoColumnTable.addCell(new Cell().add(new Paragraph(""))
                                        .setBorder(Border.NO_BORDER).setFont(bold));

                        if (req.getTotalDays() == null) {
                                req.setTotalDays("");
                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "14. Total Working Days ", String.valueOf(req.getTotalDays()));

                        if (req.getTotalPresentDays() == null) {
                                req.setTotalPresentDays("");

                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "15. Total No. Working Days when the pupil was present ",
                                        String.valueOf(req.getTotalPresentDays()));

                        if (req.getNcc() == null) {
                                req.setNcc("");
                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "16. Wheather NCC Cadet/Boys Scout/Girls Guide ",
                                        req.getNcc().toUpperCase());

                        twoColumnTable.addCell(new Cell().add(new Paragraph("(details may be given)"))
                                        .setBorder(Border.NO_BORDER).setPaddingLeft(18));

                        twoColumnTable.addCell(new Cell().add(new Paragraph(""))
                                        .setBorder(Border.NO_BORDER).setFont(bold));

                        if (req.getGames() == null) {
                                req.setGames("");
                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "17. Games Played or extra-curriculum activities in which ",
                                        req.getGames().toUpperCase());

                        twoColumnTable.addCell(new Cell()
                                        .add(new Paragraph("the pupil usually took part (mention achievemeny"))
                                        .setBorder(Border.NO_BORDER).setPaddingLeft(18));

                        twoColumnTable.addCell(new Cell().add(new Paragraph(""))
                                        .setBorder(Border.NO_BORDER).setFont(bold));

                        twoColumnTable.addCell(new Cell().add(new Paragraph("level therein)"))
                                        .setBorder(Border.NO_BORDER).setPaddingLeft(18));

                        twoColumnTable.addCell(new Cell().add(new Paragraph(""))
                                        .setBorder(Border.NO_BORDER).setFont(bold));

                        if (req.getConduct() == null) {
                                req.setConduct("");
                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "18. General Conduct ", req.getGeneralConduct());

                        if (req.getDateOfApplication() == null) {
                                req.setDateOfApplication("");
                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "19. Date of Application for certificate ", req.getDateOfApplication());

                        if (req.getDateOfIssue() == null) {
                                req.setDateOfIssue("");
                        } else if (req.getDateOfIssue().trim().isEmpty()) {
                                req.setDateOfIssue(LocalDate.now().format(
                                                DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        } else {
                                req.setDateOfIssue(req.getDateOfIssue().trim());
                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "20. Date of Issue for certificate ",
                                        req.getDateOfIssue());

                        if (req.getReason() == null) {
                                req.setReason("");
                        } else if (req.getReason().trim().isEmpty()) {
                                req.setReason("ON PARENTS REQUEST");
                        } else {
                                req.setReason(req.getReason().trim());
                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "21. Reason for Leaving the school ",
                                        req.getReason());

                        String remark = student.getGender().toLowerCase().equals("female")
                                        ? isPromotable ? "SHE HAS PASSED "
                                                        + numberToRoman(Integer.parseInt(student.getStdClass()))
                                                        + " CLASS EXAM - " + student.getSession().substring(5)
                                                        : "SHE HAS PASSED XII CLASS EXAM - "
                                                                        + student.getSession().substring(5)
                                        : isPromotable
                                                        ? "HE HAS PASSED "
                                                                        + numberToRoman(Integer.parseInt(
                                                                                        student.getStdClass()))
                                                                        + " CLASS EXAM - "
                                                                        + student.getSession().substring(5)
                                                        : "HE HAS PASSED XII CLASS EXAM - "
                                                                        + student.getSession().substring(5);

                        if (req.getRemark() == null) {
                                req.setRemark("");
                        } else if (req.getRemark().trim().isEmpty()) {
                                req.setRemark(remark);
                        } else {
                                req.setRemark(req.getRemark().trim());
                        }

                        addSingleRowTwoColumn(twoColumnTable,
                                        "22. Any other remarks ",
                                        req.getRemark());

                        doc.add(twoColumnTable);

                        // Footer
                        doc.add(new Paragraph(
                                        "Certified that the above information is in accordance with the school Admission Register.")
                                        .setFontSize(9.8f));

                        Table footerTable = new Table(UnitValue.createPercentArray(new float[] { 4, 4 }))
                                        .useAllAvailableWidth()
                                        .setMarginTop(3)
                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                        .setPaddings(0, 20, 0, 20)
                                        .setBorder(Border.NO_BORDER)
                                        .setFontSize(9.8f);

                        footerTable.addCell(
                                        new Cell().add(new Paragraph("Checked By").setPaddingLeft(45))
                                                        .setBorder(Border.NO_BORDER).setFont(bold));

                        footerTable.addCell(new Cell().add(new Paragraph("").setTextAlignment(
                                        TextAlignment.RIGHT))
                                        .setBorder(Border.NO_BORDER).setFont(bold));

                        footerTable.addCell(
                                        new Cell().add(new Paragraph("( State Full Name and Designation )"))
                                                        .setBorder(Border.NO_BORDER).setFont(bold));

                        footerTable.addCell(new Cell().add(new Paragraph("PRINCIPAL").setTextAlignment(
                                        TextAlignment.RIGHT).setPaddingRight(20))
                                        .setBorder(Border.NO_BORDER).setFont(bold));

                        doc.add(footerTable);

                        doc.close();
                        System.out.println("Transfer Certificate created successfully.");
                        return baos;
                } catch (Exception e) {
                        e.printStackTrace();
                        throw new IOException("Failed to generate TC", e);
                }
        }

        private void addSingleRow(Table table, String key, String value) throws IOException {
                table.addCell(new Cell().add(new Paragraph(key))
                                .setBorder(Border.NO_BORDER)
                                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));

                table.addCell(new Cell().add(new Paragraph(underlineIfEmpty(value, 23)))
                                .setBorder(Border.NO_BORDER)
                                .setFont((value == null || value.trim().isEmpty()) ? PdfFontFactory
                                                .createFont(StandardFonts.HELVETICA)
                                                : PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));
        }

        private void addSingleRowTwoColumn(Table table, String key, String value) throws IOException {
                table.addCell(new Cell().add(new Paragraph(key))
                                .setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph(underlineIfEmpty(value, 23)))
                                .setBorder(Border.NO_BORDER)
                                .setFont((value == null || value.trim().isEmpty()) ? PdfFontFactory
                                                .createFont(StandardFonts.HELVETICA)
                                                : PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));
        }

        private String underlineIfEmpty(String value, int length) {
                if (value == null || value.trim().isEmpty()) {
                        return "_".repeat(length);
                }
                return value;
        }

        private static final Map<Integer, String> dayMap = Map.ofEntries(
                        Map.entry(1, "FIRST"), Map.entry(2, "SECOND"), Map.entry(3, "THIRD"),
                        Map.entry(4, "FOURTH"), Map.entry(5, "FIFTH"), Map.entry(6, "SIXTH"),
                        Map.entry(7, "SEVENTH"), Map.entry(8, "EIGHTH"), Map.entry(9, "NINTH"),
                        Map.entry(10, "TENTH"), Map.entry(11, "ELEVENTH"), Map.entry(12, "TWELFTH"),
                        Map.entry(13, "THIRTEENTH"), Map.entry(14, "FOURTEENTH"), Map.entry(15, "FIFTEENTH"),
                        Map.entry(16, "SIXTEENTH"), Map.entry(17, "SEVENTEENTH"), Map.entry(18, "EIGHTEENTH"),
                        Map.entry(19, "NINETEENTH"), Map.entry(20, "TWENTIETH"), Map.entry(21, "TWENTY-FIRST"),
                        Map.entry(22, "TWENTY-SECOND"), Map.entry(23, "TWENTY-THIRD"), Map.entry(24, "TWENTY-FOURTH"),
                        Map.entry(25, "TWENTY-FIFTH"), Map.entry(26, "TWENTY-SIXTH"), Map.entry(27, "TWENTY-SEVENTH"),
                        Map.entry(28, "TWENTY-EIGHTH"), Map.entry(29, "TWENTY-NINTH"), Map.entry(30, "THIRTIETH"),
                        Map.entry(31, "THIRTY-FIRST"));

        private static final String[] units = {
                        "", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE",
                        "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN",
                        "SEVENTEEN", "EIGHTEEN", "NINETEEN"
        };

        private static final String[] tens = {
                        "", "", "TWENTY", "THIRTY", "FORTY", "FIFTY",
                        "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
        };

        public static String convertDateToWords(String dateStr) {
                LocalDate date = parseFlexibleDate(dateStr);
                int day = date.getDayOfMonth();
                String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
                int year = date.getYear();

                String dayWord = dayMap.get(day);
                String yearWord = convertYearToWords(year);

                return String.format("%s OF %s %s", dayWord, month, yearWord);
        }

        private static LocalDate parseFlexibleDate(String dateStr) {
                DateTimeFormatter[] formatters = new DateTimeFormatter[] {
                                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                                DateTimeFormatter.ISO_LOCAL_DATE, // yyyy-MM-dd
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME // yyyy-MM-ddTHH:mm:ss
                };

                for (DateTimeFormatter formatter : formatters) {
                        try {
                                TemporalAccessor parsed = formatter.parse(dateStr);
                                return LocalDate.from(parsed);
                        } catch (Exception ignored) {
                        }
                }

                throw new IllegalArgumentException("Unsupported date format: " + dateStr);
        }

        private static String convertYearToWords(int year) {
                if (year < 2000 || year > 2099)
                        return numberToWords(year); // fallback for non-2000s

                StringBuilder sb = new StringBuilder("TWO THOUSAND");
                int remainder = year % 2000;

                if (remainder > 0) {
                        if (remainder < 100)
                                sb.append(" AND ");
                        else
                                sb.append(" ");
                        sb.append(numberToWords(remainder));
                }

                return sb.toString();
        }

        private static String numberToWords(int number) {
                if (number == 0)
                        return "ZERO";
                if (number < 20)
                        return units[number];
                if (number < 100)
                        return tens[number / 10] + (number % 10 != 0 ? "-" + units[number % 10] : "");
                if (number < 1000) {
                        return units[number / 100] + " HUNDRED"
                                        + (number % 100 != 0 ? " AND " + numberToWords(number % 100) : "");
                }
                return numberToWords(number / 1000) + " THOUSAND"
                                + (number % 1000 != 0 ? " " + numberToWords(number % 1000) : "");
        }

        public static String convertDate(String inputDate) {
                SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat toFormat = new SimpleDateFormat("dd/MM/yyyy");

                try {
                        Date date = fromFormat.parse(inputDate);
                        return toFormat.format(date);
                } catch (ParseException e) {
                        e.printStackTrace(); // handle error appropriately
                        return null;
                }
        }

        public static String numberToOrdinal(int number) {
                Map<Integer, String> ordinalMap = Map.ofEntries(
                                Map.entry(1, "FIRST"), Map.entry(2, "SECOND"), Map.entry(3, "THIRD"),
                                Map.entry(4, "FOURTH"), Map.entry(5, "FIFTH"), Map.entry(6, "SIXTH"),
                                Map.entry(7, "SEVENTH"), Map.entry(8, "EIGHTH"), Map.entry(9, "NINTH"),
                                Map.entry(10, "TENTH"), Map.entry(11, "ELEVENTH"), Map.entry(12, "TWELFTH"),
                                Map.entry(13, "THIRTEENTH"), Map.entry(14, "FOURTEENTH"), Map.entry(15, "FIFTEENTH"),
                                Map.entry(16, "SIXTEENTH"), Map.entry(17, "SEVENTEENTH"), Map.entry(18, "EIGHTEENTH"),
                                Map.entry(19, "NINETEENTH"), Map.entry(20, "TWENTIETH"), Map.entry(21, "TWENTY-FIRST"),
                                Map.entry(22, "TWENTY-SECOND"), Map.entry(23, "TWENTY-THIRD"),
                                Map.entry(24, "TWENTY-FOURTH"),
                                Map.entry(25, "TWENTY-FIFTH"), Map.entry(26, "TWENTY-SIXTH"),
                                Map.entry(27, "TWENTY-SEVENTH"),
                                Map.entry(28, "TWENTY-EIGHTH"), Map.entry(29, "TWENTY-NINTH"),
                                Map.entry(30, "THIRTIETH"),
                                Map.entry(31, "THIRTY-FIRST"));

                return ordinalMap.getOrDefault(number, "UNKNOWN");
        }

        public static String numberToRoman(int number) {
                if (number < 1 || number > 3999)
                        return "INVALID";

                String[] thousands = { "", "M", "MM", "MMM" };
                String[] hundreds = { "", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM" };
                String[] tens = { "", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC" };
                String[] units = { "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX" };

                return thousands[number / 1000] +
                                hundreds[(number % 1000) / 100] +
                                tens[(number % 100) / 10] +
                                units[number % 10];
        }

}
