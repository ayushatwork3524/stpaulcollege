package com.main.stpaul.helper;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.main.stpaul.dto.response.ReceiptResponse;
import com.main.stpaul.entities.PaymentDetail;
import com.main.stpaul.entities.Student;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

public class PdfGenerator {

        public static byte[] generateReceiptPdf(Student student, ReceiptResponse receipt, PaymentDetail payment) {
                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
                        PdfDocument pdfDocument = new PdfDocument(writer);
                        Document document = new Document(pdfDocument);

                        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

                        // **Outer Border for full receipt**
                        Table outerTable = new Table(1).useAllAvailableWidth();
                        outerTable.setBorder(new SolidBorder(ColorConstants.BLACK, 2f));

                        // **Header Section**
                        outerTable.addCell(getCell(
                                        new Paragraph("VIVEKSHIL MITRA PARIWAR INSTITUTION\nST. PAUL SCHOOL (CBSE)")
                                                        .setFont(boldFont)
                                                        .setTextAlignment(TextAlignment.CENTER),
                                        true));

                        outerTable.addCell(
                                        getCell(new Paragraph(
                                                        "TO BE AFFILIATED CBSE DELHI\nNEAR BRIDGE, HUDKESHWAR, NAGPUR-440034")
                                                        .setFont(regularFont)
                                                        .setTextAlignment(TextAlignment.CENTER), true));

                        outerTable.addCell(
                                        getCell(new Paragraph("RECEIPT").setFont(boldFont)
                                                        .setTextAlignment(TextAlignment.CENTER), true));

                        // **Receipt Details Table**
                        Table detailsTable = new Table(new float[] { 3, 5, 3, 5 }).useAllAvailableWidth();
                        detailsTable.addCell(getBorderedCell("Receipt No:", boldFont));
                        detailsTable.addCell(getBorderedCell(getValueOrDefault(receipt.getReceiptNo()), regularFont));
                        detailsTable.addCell(getBorderedCell("Date:", boldFont));
                        detailsTable.addCell(getBorderedCell(
                                        getValueOrDefault(DateTimeFormater.formatDate(receipt.getPaymentDate())),
                                        regularFont));

                        detailsTable.addCell(getBorderedCell("Name:", boldFont));
                        detailsTable.addCell(getBorderedCell(
                                        getValueOrDefault(student.getFirstName() + " " + student.getSurname()),
                                        regularFont));
                        detailsTable.addCell(getBorderedCell("", boldFont));
                        detailsTable.addCell(getBorderedCell("", regularFont));

                        detailsTable.addCell(getBorderedCell("Std:", boldFont));
                        detailsTable.addCell(getBorderedCell(getValueOrDefault(student.getStdClass()), regularFont));
                        detailsTable.addCell(getBorderedCell("Section:", boldFont));
                        detailsTable.addCell(getBorderedCell(getValueOrDefault(student.getSection()), regularFont));

                        detailsTable.addCell(getBorderedCell("Admission No:", boldFont));
                        detailsTable.addCell(getBorderedCell(student.getAdmissionForm().getFormNo(), regularFont));
                        detailsTable.addCell(getBorderedCell("Academic Session:", boldFont));
                        detailsTable.addCell(getBorderedCell(getValueOrDefault(student.getSession()), regularFont));

                        detailsTable.addCell(getBorderedCell("Installment:", boldFont));
                        detailsTable.addCell(
                                        getBorderedCell(getValueOrDefault(payment.getInstallments() + " Installments"),
                                                        regularFont));
                        detailsTable.addCell(getBorderedCell("Due Date:", boldFont));
                        detailsTable.addCell(getBorderedCell(getValueOrDefault(payment.getDueDate()), regularFont));

                        outerTable.addCell(new Cell().add(detailsTable)
                                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

                        // **Fee Details Table**
                        Table feeTable = new Table(new float[] { 1, 5, 3, 1 }).useAllAvailableWidth();
                        feeTable.setBorder(new SolidBorder(ColorConstants.BLACK, 1f));

                        feeTable.addCell(getBorderedCell("Sr. No", boldFont));
                        feeTable.addCell(getBorderedCell("PARTICULARS", boldFont));
                        feeTable.addCell(getBorderedCell("Amount", boldFont));
                        feeTable.addCell(getBorderedCell("Ps.", boldFont));

                        // **Fee Data**
                        addFeeRow1(feeTable, "1.", "Admission Fee", "");
                        addFeeRow1(feeTable, "2.", "Prospectus Fee", "");
                        addFeeRow1(feeTable, "3.", "Tuition Fee", String.valueOf(receipt.getAmountPaid()));
                        addFeeRow1(feeTable, "4.", "Previous Dues", "");
                        addFeeRow1(feeTable, "5.", "Other Fee", "");

                        // **Total Calculation**
                        feeTable.addCell(getBorderedCell("", regularFont));
                        feeTable.addCell(getBorderedCell("Total", boldFont));
                        feeTable.addCell(getBorderedCell(getValueOrDefault(receipt.getAmountPaid()), boldFont));
                        feeTable.addCell(getBorderedCell("", regularFont));

                        feeTable.addCell(getBorderedCell("", regularFont));
                        feeTable.addCell(getBorderedCell("Fine", regularFont));
                        feeTable.addCell(getBorderedCell("", regularFont));
                        feeTable.addCell(getBorderedCell("", regularFont));

                        feeTable.addCell(getBorderedCell("", regularFont));
                        feeTable.addCell(getBorderedCell("Grand Total", boldFont));
                        feeTable.addCell(getBorderedCell(getValueOrDefault(receipt.getAmountPaid()), boldFont));
                        feeTable.addCell(getBorderedCell("", regularFont));

                        outerTable.addCell(
                                        new Cell().add(feeTable).setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

                        outerTable.addCell(new Cell()
                                        .add(new Paragraph(
                                                        "Amount (In word) "
                                                                        + NumberToWordConverter.convert(
                                                                                        receipt.getAmountPaid())
                                                                        + "  ONLY")
                                                        .setFont(boldFont))
                                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

                        // **Signatures Section**
                        Table signTable = new Table(new float[] { 1, 1, 1 }).useAllAvailableWidth();
                        signTable.addCell(getSignatureCell("Clerk"));
                        signTable.addCell(getSignatureCell("Cashier"));
                        signTable.addCell(getSignatureCell("Accountant"));

                        outerTable.addCell(
                                        new Cell().add(signTable).setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

                        document.add(outerTable);
                        document.close();

                        return byteArrayOutputStream.toByteArray();

                } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                }
        }

        private static String getValueOrDefault(Object value) {
                return (value == null || value.toString().trim().isEmpty()) ? "-" : value.toString();
        }

        // Helper method for bordered cells (for "Particulars" table)
        private static Cell getBorderedCell(String text, PdfFont font) {
                return new Cell().add(new Paragraph(text).setFont(font))
                                .setBorder(new SolidBorder(ColorConstants.BLACK, 1f))
                                .setPadding(5)
                                .setTextAlignment(TextAlignment.LEFT);
        }

        private static Cell getBorderedCell1(String text, PdfFont font) {
                return new Cell().add(new Paragraph(text).setFont(font))
                                // .setBorder(new SolidBorder(ColorConstants.BLACK, 1f))
                                .setPadding(5)
                                .setTextAlignment(TextAlignment.LEFT);
        }

        // Helper method for borderless header cells
        private static Cell getCell(Paragraph paragraph, boolean border) {
                return new Cell().add(paragraph)
                                .setBorder(border ? new SolidBorder(ColorConstants.BLACK, 1f) : SolidBorder.NO_BORDER)
                                .setPadding(5);
        }

        private static void addFeeRow1(Table table, String srNo, String particulars, String amount) throws IOException {
                table.addCell(getBorderedCell1(srNo, PdfFontFactory.createFont(StandardFonts.HELVETICA)))
                                .setBorder(null);
                table.addCell(getBorderedCell1(particulars, PdfFontFactory.createFont(StandardFonts.HELVETICA)))
                                .setBorder(null);
                table.addCell(getBorderedCell1(amount, PdfFontFactory.createFont(StandardFonts.HELVETICA)))
                                .setBorder(null);
                table.addCell(getBorderedCell1("", PdfFontFactory.createFont(StandardFonts.HELVETICA))).setBorder(null);
        }

        // private static void addFeeRow(Table table, String srNo, String particulars,
        // String amount) throws IOException {
        // table.addCell(getBorderedCell(srNo,
        // PdfFontFactory.createFont(StandardFonts.HELVETICA)));
        // table.addCell(getBorderedCell(particulars,
        // PdfFontFactory.createFont(StandardFonts.HELVETICA)));
        // table.addCell(getBorderedCell(amount,
        // PdfFontFactory.createFont(StandardFonts.HELVETICA)));
        // table.addCell(getBorderedCell("",
        // PdfFontFactory.createFont(StandardFonts.HELVETICA)));
        // }

        // Helper method to create signature cell
        private static Cell getSignatureCell(String text) throws IOException {
                return new Cell().add(new Paragraph(text).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)))
                                .setTextAlignment(TextAlignment.CENTER)
                                .setPadding(10)
                                .setBorder(Border.NO_BORDER);
        }

        // new
        public static byte[] generateReceipt(Student student, ReceiptResponse receipt,
                        PaymentDetail payment) throws IOException {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                PdfWriter writer = new PdfWriter(byteArrayOutputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument, PageSize.A4);
                document.setMargins(20, 20, 20, 20);
                document.setBorder(new SolidBorder(ColorConstants.BLACK, 2f));

                InputStream logoStream = new ClassPathResource("logo.png").getInputStream();
                byte[] logoBytes = logoStream.readAllBytes();
                Image logo = new Image(ImageDataFactory.create(logoBytes));
                logo.setHeight(60);
                logo.setAutoScaleWidth(true);

                int SINGLE_SPACE = 30;

                int DOUBLE_SPACE = 14;

                PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

                PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

                Table header = new Table(UnitValue.createPercentArray(new float[] { 1, 4 }))

                                .useAllAvailableWidth();
                header.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
                Cell titleCell = new Cell().add(new Paragraph(
                                "VIVEKSHIL MITRA PARIWAR INSTITUTION")
                                .setFontSize(15).setTextAlignment(TextAlignment.CENTER))
                                .add(new Paragraph(
                                                "ST.  PAUL SCHOOL (CBSE)")
                                                .setPadding(0)
                                                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                                                .setFontSize(32).setTextAlignment(TextAlignment.CENTER))
                                .add(new Paragraph(
                                                "TO BE AFFILIATED CBSE DELHI\nNEAR BRIDGE, HUDKESHWAR, NAGPUR-440034")
                                                .setFontSize(15).setTextAlignment(TextAlignment.CENTER));
                header.addCell(titleCell.setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));

                header.addCell(new Cell(1, 4).add(new Paragraph("RECEIPT").setFontSize(30)
                                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
                                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));

                header.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 2f));
                document.add(header);

                // Receipt Info
                Table firstTable = new Table(UnitValue.createPercentArray(new float[] { 1f, 2.5f, 1f, 1f }))
                                .useAllAvailableWidth()
                                .setMargins(10, 5, 0, 5)
                                .setBorder(Border.NO_BORDER).setFontSize(15);

                firstTable.addCell(new Cell().add(new Paragraph("Receipt No."))
                                .setBorder(Border.NO_BORDER).setFont(bold));

                firstTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                receipt.getReceiptNo(), DOUBLE_SPACE))).setBorder(Border.NO_BORDER));

                firstTable.addCell(new Cell().add(new Paragraph("Date"))
                                .setBorder(Border.NO_BORDER).setFont(bold).setTextAlignment(TextAlignment.RIGHT));

                firstTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                convertFullDate(receipt.getPaymentDate()
                                                .toString()),
                                DOUBLE_SPACE))).setBorder(Border.NO_BORDER));

                document.add(firstTable);

                Table secondTable = new Table(UnitValue.createPercentArray(new float[] { 0.18f, 0.82f }))
                                .useAllAvailableWidth()
                                .setMargins(0, 5, 0, 5)
                                .setBorder(Border.NO_BORDER).setFontSize(15);

                secondTable.addCell(new Cell().add(new Paragraph("Name"))
                                .setBorder(Border.NO_BORDER).setFont(bold));

                secondTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                student.getFullName(), SINGLE_SPACE))).setBorder(Border.NO_BORDER));

                document.add(secondTable);

                Table thirdTable = new Table(UnitValue.createPercentArray(new float[] { 1f, 2.5f, 1f,
                                1f }))
                                .useAllAvailableWidth()
                                .setMargins(0, 5, 0, 5)
                                .setBorder(Border.NO_BORDER).setFontSize(15);

                thirdTable.addCell(new Cell().add(new Paragraph("Std."))
                                .setBorder(Border.NO_BORDER).setFont(bold));

                thirdTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                numberToRoman(Integer.parseInt(student.getStdClass())), DOUBLE_SPACE)))
                                .setBorder(Border.NO_BORDER));

                thirdTable.addCell(new Cell().add(new Paragraph("Section"))
                                .setBorder(Border.NO_BORDER).setFont(bold).setTextAlignment(TextAlignment.RIGHT));

                thirdTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                student.getSection(), DOUBLE_SPACE))).setBorder(Border.NO_BORDER));

                document.add(thirdTable);

                Table fourthTable = new Table(UnitValue.createPercentArray(new float[] { 1.1f, 1.5f, 1.5f,
                                1f }))
                                .useAllAvailableWidth()
                                .setMargins(0, 5, 0, 5)
                                .setBorder(Border.NO_BORDER).setFontSize(15);

                fourthTable.addCell(new Cell().add(new Paragraph("Admission No."))
                                .setBorder(Border.NO_BORDER).setFont(bold));

                fourthTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                student.getAdmissionForm().getFormNo(), DOUBLE_SPACE))).setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.LEFT));

                fourthTable.addCell(new Cell().add(new Paragraph("Academic Session"))
                                .setBorder(Border.NO_BORDER).setFont(bold).setTextAlignment(TextAlignment.RIGHT));

                fourthTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                student.getSession(), DOUBLE_SPACE))).setBorder(Border.NO_BORDER).setPaddingLeft(5));

                document.add(fourthTable);

                Table fifthTable = new Table(UnitValue.createPercentArray(new float[] { 1f, 2.7f, 1f,
                                1f }))
                                .useAllAvailableWidth()
                                .setMargins(0, 5, 0, 5)
                                .setBorder(Border.NO_BORDER).setFontSize(15);

                fifthTable.addCell(new Cell().add(new Paragraph("Installment"))
                                .setBorder(Border.NO_BORDER).setFont(bold));

                fifthTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                payment.getInstallments() + " Installment", DOUBLE_SPACE)))
                                .setBorder(Border.NO_BORDER));

                fifthTable.addCell(new Cell().add(new Paragraph("Due Date"))
                                .setBorder(Border.NO_BORDER).setFont(bold).setTextAlignment(TextAlignment.RIGHT));

                fifthTable.addCell(new Cell().add(new Paragraph(underlineIfEmpty(
                                convertDate(payment.getDueDate().toString()), 10))).setBorder(Border.NO_BORDER));

                document.add(fifthTable);

                Table sixthTable = new Table(UnitValue.createPercentArray(new float[] { 1, 3.5f, 2 }))
                                .useAllAvailableWidth()
                                .setBorderBottom(new SolidBorder(2))
                                .setFontSize(15).setBorderTop(new SolidBorder(2));

                sixthTable.addCell(new Cell().add(new Paragraph("Sr. No."))
                                .setFont(bold).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                                .setPaddings(5, 0, 5, 0));

                sixthTable.addCell(new Cell().add(new Paragraph("PARTICULARS"))
                                .setFont(bold).setBorderRight(new SolidBorder(2)).setBorderLeft(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.CENTER).setPaddings(5, 0, 5, 0));

                Table rsTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1 }))
                                .useAllAvailableWidth();

                rsTable.addCell(new Cell()
                                .add(new Paragraph("Rs.").setFont(normal).setFontSize(12)
                                                .setTextAlignment(TextAlignment.LEFT))
                                .setBorder(Border.NO_BORDER).setPadding(0));

                rsTable.addCell(new Cell()
                                .add(new Paragraph("Ps.").setFont(normal).setFontSize(12)
                                                .setTextAlignment(TextAlignment.RIGHT))
                                .setBorder(Border.NO_BORDER).setPadding(0));

                sixthTable.addCell(new Cell().add(new Paragraph("Amount"))
                                .setFont(bold).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                                .setPaddings(5, 10, 5, 10)
                                .add(rsTable)

                );

                document.add(sixthTable);

                Table seventhTable = new Table(UnitValue.createPercentArray(new float[] { 1, 3.5f, 2 }))
                                .setBorder(Border.NO_BORDER)
                                .useAllAvailableWidth()
                                .setBorderBottom(new SolidBorder(2))
                                .setFontSize(15);

                String[][] feeRows = {
                                { "1.", "Admission Fee", "", "" },
                                { "2.", "Prospectus Fee", "", "" },
                                { "3.", "Tution Fee", String.valueOf(payment
                                                .getPaidAmount()), "" },
                                { "4.", "Previous Dues", "", "" },
                                { "5.", "Other Fee", "", "" }
                };

                // Iterate through rows
                for (int i = 0; i < feeRows.length; i++) {
                        String[] row = feeRows[i];

                        // 1st Column
                        seventhTable.addCell(new Cell()
                                        .add(new Paragraph(row[0]))
                                        .setBorder(Border.NO_BORDER)
                                        .setTextAlignment(TextAlignment.CENTER)
                                        .setPaddingLeft(20));

                        // 2nd Column
                        seventhTable.addCell(new Cell()
                                        .add(new Paragraph(row[1]))
                                        .setBorder(Border.NO_BORDER)
                                        .setBorderRight(new SolidBorder(2))
                                        .setTextAlignment(TextAlignment.LEFT)
                                        .setPaddingLeft(20));

                        // Inner amountTable for the 3rd and 4th columns
                        Table amountTable = new Table(UnitValue.createPercentArray(new float[] { 2, 1 }))
                                        .setBorder(Border.NO_BORDER)
                                        .setPadding(0)
                                        .useAllAvailableWidth();

                        // For the 3rd column: Add content if non-empty, else use a placeholder
                        String column3Value = (row[2] == null || row[2].isEmpty()) ? "\u00A0" : row[2];

                        // Add 3rd column (span this cell across multiple rows)
                        amountTable.addCell(new Cell()
                                        .add(new Paragraph(column3Value)
                                                        .setFont(normal)
                                                        .setTextAlignment(TextAlignment.CENTER))
                                        .setBorder(Border.NO_BORDER)
                                        .setPadding(0)
                                        .setTextAlignment(TextAlignment.CENTER));

                        // 4th Column
                        amountTable.addCell(new Cell()
                                        .add(new Paragraph(row[3])
                                                        .setFont(normal)
                                                        .setTextAlignment(TextAlignment.CENTER))
                                        .setBorder(Border.NO_BORDER)
                                        .setPadding(0)
                                        .setTextAlignment(TextAlignment.CENTER));

                        // Wrap amountTable in a cell
                        Cell amountWrapperCell = new Cell()
                                        .add(amountTable)
                                        .setBorder(Border.NO_BORDER);

                        // Add the amount wrapper cell to the main table
                        seventhTable.addCell(amountWrapperCell);

                        PdfCanvas pdfCanvas = new PdfCanvas(pdfDocument.getFirstPage());

                        float xLine = 520;
                        float yBottom = 237;
                        float yTop = 460;

                        pdfCanvas.setLineWidth(1.8f);

                        pdfCanvas.moveTo(xLine, yBottom);

                        pdfCanvas.lineTo(xLine, yTop);

                        pdfCanvas.stroke();
                }

                document.add(seventhTable);

                Table eightthTable = new Table(UnitValue.createPercentArray(new float[] { 1, 2f, 1.5f, 1, 1 }))
                                .useAllAvailableWidth()
                                .setBorderBottom(new SolidBorder(2))
                                .setFontSize(15);

                String[][] totalRows = {
                                { "", "", "Total", String.valueOf(payment.getPaidAmount()), "" },
                                { "", "", "Fine", "", "" },
                                { "", "", "Grand Total", String.valueOf(payment.getPaidAmount()), "" },
                };

                for (String[] row : totalRows) {

                        eightthTable.addCell(new Cell().add(new Paragraph(row[0]))
                                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                                        .setPaddingLeft(20));

                        eightthTable.addCell(new Cell().add(new Paragraph(row[1]))
                                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                                        .setPaddingLeft(20));

                        eightthTable.addCell(new Cell().add(new Paragraph(row[2]))
                                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                                        .setBorderRight(new SolidBorder(2))
                                        .setBorderLeft(new SolidBorder(2))
                                        .setBorderBottom(new SolidBorder(2))
                                        .setFont(bold)
                                        .setPaddingLeft(20));

                        eightthTable.addCell(new Cell().add(new Paragraph(row[3]))
                                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                                        .setBorderBottom(new SolidBorder(2))
                                        .setPaddingLeft(20));

                        eightthTable.addCell(new Cell().add(new Paragraph(row[4]))
                                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                                        .setBorderBottom(new SolidBorder(2))
                                        .setPaddingLeft(20));

                }

                document.add(eightthTable);

                Paragraph amountInWords = new Paragraph("Amount (In word) ").setFont(bold)
                                .add(new Paragraph(convertDecimalToWords(payment.getPaidAmount())).setFont(normal))
                                .setPaddings(10, 10, 50, 20)
                                .setFontSize(15)
                                .setBorderBottom(new SolidBorder(2));

                document.add(amountInWords);

                document.add(new Paragraph("\n".repeat(4)));

                Table lastTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 1 }))
                                .useAllAvailableWidth()
                                .setBorder(Border.NO_BORDER)
                                .setPaddings(30, 20, 20, 20)
                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                .setVerticalAlignment(VerticalAlignment.BOTTOM)
                                .setFont(bold)
                                .setFontSize(15);

                lastTable.addCell(new Cell().add(new Paragraph("Clerk"))
                                .setBorder(Border.NO_BORDER));
                lastTable.addCell(new Cell().add(new Paragraph("Cashier"))
                                .setTextAlignment(TextAlignment.CENTER)
                                .setBorder(Border.NO_BORDER));
                lastTable.addCell(new Cell().add(new Paragraph("Accountant"))
                                .setTextAlignment(TextAlignment.RIGHT)
                                .setBorder(Border.NO_BORDER));

                document.add(lastTable);

                float margin = 20f;
                float borderRadius = 3f;

                PdfCanvas canvas = new PdfCanvas(pdfDocument.getFirstPage());
                Rectangle rect = pdfDocument.getFirstPage().getPageSize();

                canvas.setStrokeColor(ColorConstants.BLACK);
                canvas.setLineWidth(1.5f);
                canvas.roundRectangle(
                                rect.getLeft() + margin,
                                rect.getBottom() + margin,
                                rect.getWidth() - 2 * margin,
                                rect.getHeight() - 2 * margin,
                                borderRadius);
                canvas.stroke();

                document.close();

                return byteArrayOutputStream.toByteArray();
        }

        private static String underlineIfEmpty(String value, int length) {
                if (value == null || value.trim().isEmpty()) {
                        return "_".repeat(length);
                }
                return value;
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

        private static final String[] units = {
                        "", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE",
                        "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN",
                        "SEVENTEEN", "EIGHTEEN", "NINETEEN"
        };

        private static final String[] tens = {
                        "", "", "TWENTY", "THIRTY", "FORTY", "FIFTY",
                        "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
        };

        public static String convertDecimalToWords(double number) {
                long wholePart = (long) number;
                int decimalPart = (int) Math.round((number - wholePart) * 100);

                StringBuilder result = new StringBuilder();

                if (wholePart > 0) {
                        result.append(numberToWords((int) wholePart));
                }

                if (decimalPart > 0) {
                        if (result.length() > 0)
                                result.append(" AND ");
                        result.append(convertFraction(decimalPart));
                }

                result.append(" ONLY");

                return result.toString().trim();
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

        private static String convertFraction(int fraction) {
                if (fraction % 10 == 0) {
                        return numberToWords(fraction / 10) + " TENTHS";
                } else {
                        return numberToWords(fraction) + " HUNDREDTHS";
                }
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

        public static String convertFullDate(String isoDateTime) {
                LocalDateTime dateTime = LocalDateTime.parse(isoDateTime);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return dateTime.format(formatter);
        }

}
