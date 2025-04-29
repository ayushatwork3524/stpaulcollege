// package com.main.stpaul.services.impl;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.time.format.TextStyle;
// import java.util.Locale;
// import java.util.Map;

// import org.springframework.core.io.ClassPathResource;
// import org.springframework.stereotype.Service;

// import com.itextpdf.io.font.constants.StandardFonts;
// import com.itextpdf.io.image.ImageDataFactory;
// import com.itextpdf.kernel.font.PdfFont;
// import com.itextpdf.kernel.font.PdfFontFactory;
// import com.itextpdf.kernel.pdf.PdfDocument;
// import com.itextpdf.kernel.pdf.PdfWriter;
// import com.itextpdf.layout.Document;
// import com.itextpdf.layout.borders.SolidBorder;
// import com.itextpdf.layout.element.Cell;
// import com.itextpdf.layout.element.Image;
// import com.itextpdf.layout.element.Paragraph;
// import com.itextpdf.layout.element.Table;
// import com.itextpdf.layout.properties.TextAlignment;
// import com.itextpdf.layout.properties.UnitValue;
// import com.main.stpaul.entities.Student;
// import com.main.stpaul.services.serviceInterface.StudentService;
// import com.main.stpaul.services.serviceInterface.LCService;

// @Service
// public class LCServiceImpl implements LCService {

// private final StudentService studentService;

// public LCServiceImpl(StudentService studentService) {
// this.studentService = studentService;
// }

// @Override
// public ByteArrayOutputStream generateLc(String studentId) throws IOException
// {

// int SPACE_LENGTH = 20;

// Student student = this.studentService.getStudentById(studentId);

// ByteArrayOutputStream baos = new ByteArrayOutputStream();
// PdfWriter writer = new PdfWriter(baos);
// PdfDocument pdf = new PdfDocument(writer);
// Document document = new Document(pdf);
// document.setMargins(20, 20, 20, 20);

// var bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
// var normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

// if (student.getLeavingCertificates().size() > 0) {

// document.add(new Paragraph(
// getClassSuffix(String.valueOf(student.getLeavingCertificates().size()))
// + " Copy")
// .setFont(bold)
// .setFontSize(9)
// .setTextAlignment(TextAlignment.RIGHT));
// }

// // Header
// document.add(new Paragraph("VIVEKSHIL MITRA PARIWAR INSTITUTION")
// .setFontSize(8).setTextAlignment(TextAlignment.CENTER)
// .setMarginBottom(2));

// document.add(new Paragraph("ST. PAUL SCIENCE & COMMERCE\nJUNIOR COLLEGE")
// .setFont(bold).setFontSize(12).setTextAlignment(TextAlignment.CENTER)
// .setMarginBottom(2));

// document.add(new Paragraph("Near Bridge, Hudkeshwar Road, Nagpur-440 034
// (Maharashtra)\n" +
// "Email - info@stpaulnagpur.com Website - www.stpaulcollegenp.com")
// .setFont(normal).setFontSize(8).setTextAlignment(TextAlignment.CENTER)
// .setMarginBottom(2));

// document.add(new Paragraph(
// "UDISE No : " + underlineIfEmpty("27090908706", 12) + " "
// + " "
// + "Admission No. : "
// + underlineIfEmpty(
// String.valueOf(student.getAdmissionForm().getFormNo()),
// 12))
// .setFont(normal).setFontSize(10).setTextAlignment(TextAlignment.CENTER)
// .setMarginBottom(4));

// document.add(new Paragraph("LEAVING CERTIFICATE")
// .setFont(bold).setTextAlignment(TextAlignment.CENTER).setFontSize(14).setUnderline());

// document.add(
// new Paragraph("Student Id : " +
// underlineIfEmpty(String.valueOf(student.getStudentId()),
// SPACE_LENGTH))
// .setFont(normal).setFontSize(10));
// document.add(
// new Paragraph("UID No. (Aadhar card no. of student) : "
// + underlineIfEmpty(String.valueOf(student.getAdharNo()), SPACE_LENGTH))
// .setFont(normal)
// .setFontSize(10));

// // Table for contents
// Table table = new Table(UnitValue.createPercentArray(new float[] { 2, 5 }))
// .useAllAvailableWidth().setMarginTop(10f);

// // Helper method to add row
// addRow(table, "1) Name of Student",
// underlineIfEmpty(
// student.getFirstName().toUpperCase() + " "
// + student.getFatherName().toUpperCase() + " "
// + student.getSurname().toUpperCase(),
// SPACE_LENGTH),
// bold, normal);

// addComplexRow(table, "2) Father's name",
// underlineIfEmpty(student.getFatherName().toUpperCase(), SPACE_LENGTH), "
// (Surname) ",
// underlineIfEmpty(student.getSurname().toUpperCase(), SPACE_LENGTH),
// bold, normal, 10);

// addRow(table, "3) Mother's Name",
// underlineIfEmpty(student.getMotherName().toUpperCase(), SPACE_LENGTH),
// bold,
// normal);
// addRow(table, "4) Nationality", underlineIfEmpty("", SPACE_LENGTH), bold,
// normal);
// addRow(table, "5) Mother Tongue", underlineIfEmpty("MARATHI", SPACE_LENGTH),
// bold, normal);
// addRow(table, "6) Religion", underlineIfEmpty("HINDU", SPACE_LENGTH), bold,
// normal);
// addComplexRow(table, "7) Caste",
// underlineIfEmpty(student.getCaste().toUpperCase(), SPACE_LENGTH), " Sub Caste
// ",
// underlineIfEmpty("", SPACE_LENGTH),
// bold, normal, 10);

// addMultiLineComplexRow(table,
// "8) Place of Birth (City/Town)",
// underlineIfEmpty("", SPACE_LENGTH), " Tah: ", underlineIfEmpty("",
// SPACE_LENGTH),
// "District: ", underlineIfEmpty("", SPACE_LENGTH),
// "State: ", underlineIfEmpty("MAHARASHTRA", SPACE_LENGTH),
// "Country: ", underlineIfEmpty("INDIA", SPACE_LENGTH),
// bold, normal, 10);

// addRow(table, "9) Date of Birth (As christian calender)",
// underlineIfEmpty(student.getDateOfBirth().toString(), SPACE_LENGTH), bold,
// normal);

// addRow(table, "Date of Birth (In Word)", underlineIfEmpty(convertDateToWords(
// student.getDateOfBirth().toString()), SPACE_LENGTH), bold, normal);

// addRow(table, "10) Previous School Name",
// underlineIfEmpty(student.getLastSchool().getCollegeName().toUpperCase(),
// SPACE_LENGTH),
// bold, normal);

// addRow(table, "& Class", underlineIfEmpty("", SPACE_LENGTH), bold, normal);

// addComplexRow(table, "11) Date of Admission in Existing school",
// underlineIfEmpty(student.getAdmissionDate().toString(), SPACE_LENGTH), "
// (Class) ",
// underlineIfEmpty(student.getStdClass(), SPACE_LENGTH),
// bold, normal, 10);

// addComplexRow(table, "12) Progress in Studies",
// underlineIfEmpty("SATISFACTORY", SPACE_LENGTH), " Conduct: ",
// underlineIfEmpty("GOOD", SPACE_LENGTH),
// bold, normal, 10);

// addRow(table, "13) Date of Leaving College",
// underlineIfEmpty(convertToSimpleDate(LocalDateTime.now().toString()),
// SPACE_LENGTH),
// bold, normal);

// addComplexRow(table, "14) Class in which Studying",
// underlineIfEmpty(student.getStdClass(), SPACE_LENGTH), " and Since When : ",
// underlineIfEmpty(student.getSession(), SPACE_LENGTH),
// bold, normal, 10);

// addRow(table, "15) Last Result", underlineIfEmpty("", SPACE_LENGTH), bold,
// normal);
// addRow(table, "16) Reason of Leaving College", underlineIfEmpty("ON PARENTS
// REQUEST", SPACE_LENGTH),
// bold,
// normal);
// addRow(table, "17) Remark", "SHE HAS JOIN COLLEGE & LEFT ON DATE "
// + underlineIfEmpty(student.getAdmissionDate().toString(), SPACE_LENGTH),
// bold, normal);

// document.add(table);

// // Note section
// document.add(new Paragraph(
// "Note:- No Change in any entry in this certificate shall be made except by
// the authority issuing it and any infringement of this requirement is liable
// to involve the imposition of penalty such as that of rectification.")
// .setFont(normal).setFontSize(8).setMarginTop(5));

// document.add(new Paragraph("Date: " +
// underlineIfEmpty(convertToSimpleDate(LocalDateTime.now()
// .toString()), SPACE_LENGTH))
// .setFont(normal).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setMarginTop(10));

// document.add(new Paragraph("PRINCIPAL\nST PAUL JR. COLLEGE\nHUDKESHWAR ROAD
// NAGPUR")
// .setFont(bold).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));

// // Optional: Add stamp image if available in resources
// try {
// InputStream stampStream = new
// ClassPathResource("stamp.png").getInputStream(); // Place
// // "stamp.png"
// // under
// // /resources/
// Image stamp = new Image(ImageDataFactory.create(stampStream.readAllBytes()));
// stamp.scaleToFit(80, 80).setFixedPosition(70, 70); // Adjust position as
// needed
// document.add(stamp);
// } catch (Exception e) {
// System.out.println("Stamp not found.");
// }

// document.close();

// return baos; // Return PDF as ByteArrayOutputStream
// }

// private void addRow(Table table, String label, String value,
// com.itextpdf.kernel.font.PdfFont labelFont,
// com.itextpdf.kernel.font.PdfFont valueFont) {
// table.addCell(new Cell()
// .add(new
// Paragraph(label).setFont(labelFont).setFontSize(10).setMultipliedLeading(1f))
// .setBorder(SolidBorder.NO_BORDER).setPadding(5));

// table.addCell(new Cell()
// .add(new Paragraph(value).setFont(valueFont).setFontSize(10).setUnderline()
// .setMultipliedLeading(1f))
// .setBorder(SolidBorder.NO_BORDER).setPadding(5));
// }

// private void addComplexRow(Table table, String label,
// String value1, String subLabel, String value2,
// PdfFont labelFont, PdfFont valueFont, float fontSize) {

// Cell cell1 = new Cell()
// .add(new Paragraph(label).setFont(labelFont).setFontSize(fontSize))
// .setBorder(SolidBorder.NO_BORDER).setPadding(5);

// Paragraph valuePara = new Paragraph()
// .add(new com.itextpdf.layout.element.Text(value1 + " ").setFont(valueFont)
// .setFontSize(fontSize)
// .setUnderline())
// .add(new com.itextpdf.layout.element.Text(subLabel).setFont(labelFont)
// .setFontSize(fontSize))
// .add(new com.itextpdf.layout.element.Text(" " + value2).setFont(valueFont)
// .setFontSize(fontSize)
// .setUnderline());

// Cell cell2 = new Cell()
// .add(valuePara)
// .setBorder(SolidBorder.NO_BORDER).setPadding(5);

// table.addCell(cell1);
// table.addCell(cell2);
// }

// private void addMultiLineComplexRow(Table table, String label,
// String line1Value, String line1SubLabel, String line1SubValue,
// String line2Label1, String line2Value1,
// String line2Label2, String line2Value2,
// String line2Label3, String line2Value3,
// PdfFont boldFont, PdfFont normalFont, float fontSize) {

// Cell cell1 = new Cell()
// .add(new Paragraph(label).setFont(boldFont).setFontSize(fontSize))
// .setBorder(SolidBorder.NO_BORDER).setPadding(5);

// Paragraph valuePara = new Paragraph()
// .add(new com.itextpdf.layout.element.Text(underlineIfEmpty(line1Value, 12) +
// " ")
// .setFont(normalFont)
// .setFontSize(fontSize).setUnderline()) // ✅
// .add(new com.itextpdf.layout.element.Text(line1SubLabel).setFont(boldFont)
// .setFontSize(fontSize))
// .add(new com.itextpdf.layout.element.Text(underlineIfEmpty(line1SubValue,
// 10))
// .setFont(normalFont)
// .setFontSize(fontSize).setUnderline()) // ✅
// .add("\n")
// .add(new com.itextpdf.layout.element.Text(line2Label1).setFont(boldFont)
// .setFontSize(fontSize))
// .add(new com.itextpdf.layout.element.Text(underlineIfEmpty(line2Value1, 12) +
// " ")
// .setFont(normalFont).setFontSize(fontSize).setUnderline()) // ✅
// .add(new com.itextpdf.layout.element.Text(line2Label2).setFont(boldFont)
// .setFontSize(fontSize))
// .add(new com.itextpdf.layout.element.Text(underlineIfEmpty(line2Value2, 12) +
// " ")
// .setFont(normalFont).setFontSize(fontSize).setUnderline()) // ✅
// .add(new com.itextpdf.layout.element.Text(line2Label3).setFont(boldFont)
// .setFontSize(fontSize))
// .add(new com.itextpdf.layout.element.Text(underlineIfEmpty(line2Value3, 10))
// .setFont(normalFont)
// .setFontSize(fontSize).setUnderline()); // ✅

// Cell cell2 = new Cell().add(valuePara)
// .setBorder(SolidBorder.NO_BORDER).setPadding(5);

// table.addCell(cell1);
// table.addCell(cell2);
// }

// private String underlineIfEmpty(String value, int length) {
// if (value == null || value.trim().isEmpty()) {
// return "_".repeat(length); // this will show visible underline placeholders
// }
// return value;
// }

// public String getClassSuffix(String stdClass) {
// if (stdClass == null || stdClass.trim().isEmpty())
// return "";

// try {
// int number = Integer.parseInt(stdClass.trim());

// if (number % 100 >= 11 && number % 100 <= 13) {
// return number + "th";
// }

// return switch (number % 10) {
// case 1 -> number + "st";
// case 2 -> number + "nd";
// case 3 -> number + "rd";
// default -> number + "th";
// };
// } catch (NumberFormatException e) {
// return stdClass; // In case it's not a number (like "Nursery")
// }
// }

// private static final Map<Integer, String> dayMap = Map.ofEntries(
// Map.entry(1, "FIRST"), Map.entry(2, "SECOND"), Map.entry(3, "THIRD"),
// Map.entry(4, "FOURTH"), Map.entry(5, "FIFTH"), Map.entry(6, "SIXTH"),
// Map.entry(7, "SEVENTH"), Map.entry(8, "EIGHTH"), Map.entry(9, "NINTH"),
// Map.entry(10, "TENTH"), Map.entry(11, "ELEVENTH"), Map.entry(12, "TWELFTH"),
// Map.entry(13, "THIRTEENTH"), Map.entry(14, "FOURTEENTH"), Map.entry(15,
// "FIFTEENTH"),
// Map.entry(16, "SIXTEENTH"), Map.entry(17, "SEVENTEENTH"), Map.entry(18,
// "EIGHTEENTH"),
// Map.entry(19, "NINETEENTH"), Map.entry(20, "TWENTIETH"), Map.entry(21,
// "TWENTY-FIRST"),
// Map.entry(22, "TWENTY-SECOND"), Map.entry(23, "TWENTY-THIRD"), Map.entry(24,
// "TWENTY-FOURTH"),
// Map.entry(25, "TWENTY-FIFTH"), Map.entry(26, "TWENTY-SIXTH"), Map.entry(27,
// "TWENTY-SEVENTH"),
// Map.entry(28, "TWENTY-EIGHTH"), Map.entry(29, "TWENTY-NINTH"), Map.entry(30,
// "THIRTIETH"),
// Map.entry(31, "THIRTY-FIRST"));

// private static final String[] units = {
// "", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE",
// "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN",
// "SEVENTEEN", "EIGHTEEN", "NINETEEN"
// };

// private static final String[] tens = {
// "", "", "TWENTY", "THIRTY", "FORTY", "FIFTY",
// "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
// };

// public static String convertDateToWords(String dateStr) {
// LocalDate date = LocalDate.parse(dateStr);
// int day = date.getDayOfMonth();
// String month = date.getMonth().getDisplayName(TextStyle.FULL,
// Locale.ENGLISH).toUpperCase();
// int year = date.getYear();

// String dayWord = dayMap.get(day);
// String yearWord = convertYearToWords(year);

// return String.format("%s OF %s %s", dayWord, month, yearWord);
// }

// private static String convertYearToWords(int year) {
// if (year < 2000 || year > 2099)
// return numberToWords(year); // fallback for non-2000s

// StringBuilder sb = new StringBuilder("TWO THOUSAND");
// int remainder = year % 2000;

// if (remainder > 0) {
// if (remainder < 100)
// sb.append(" AND ");
// else
// sb.append(" ");
// sb.append(numberToWords(remainder));
// }

// return sb.toString();
// }

// private static String numberToWords(int number) {
// if (number == 0)
// return "ZERO";
// if (number < 20)
// return units[number];
// if (number < 100)
// return tens[number / 10] + (number % 10 != 0 ? "-" + units[number % 10] :
// "");
// if (number < 1000) {
// return units[number / 100] + " HUNDRED"
// + (number % 100 != 0 ? " AND " + numberToWords(number % 100) : "");
// }
// return numberToWords(number / 1000) + " THOUSAND"
// + (number % 1000 != 0 ? " " + numberToWords(number % 1000) : "");
// }

// public static String convertToSimpleDate(String isoDateTime) {
// LocalDateTime dateTime = LocalDateTime.parse(isoDateTime);
// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
// return dateTime.format(formatter);
// }

// }
