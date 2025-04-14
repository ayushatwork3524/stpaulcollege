package com.main.stpaul.helper;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
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

public class PdfGenerator {

    public static byte[] generateReceiptPdf(Student student,ReceiptResponse receipt,PaymentDetail payment) {
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
            outerTable.addCell(getCell(new Paragraph("VIVEKSHIL MITRA PARIWAR INSTITUTION\nST. PAUL SCHOOL (CBSE)")
                    .setFont(boldFont)
                    .setTextAlignment(TextAlignment.CENTER), true));

            outerTable.addCell(getCell(new Paragraph("TO BE AFFILIATED CBSE DELHI\nNEAR BRIDGE, HUDKESHWAR, NAGPUR-440034")
                    .setFont(regularFont)
                    .setTextAlignment(TextAlignment.CENTER), true));

            outerTable.addCell(getCell(new Paragraph("RECEIPT").setFont(boldFont).setTextAlignment(TextAlignment.CENTER), true));

            // **Receipt Details Table**
            Table detailsTable = new Table(new float[]{3, 5, 3, 5}).useAllAvailableWidth();
            detailsTable.addCell(getBorderedCell("Receipt No:", boldFont));
            detailsTable.addCell(getBorderedCell(getValueOrDefault(receipt.getReceiptNo()), regularFont));
            detailsTable.addCell(getBorderedCell("Date:", boldFont));
            detailsTable.addCell(getBorderedCell(getValueOrDefault(DateTimeFormater.formatDate(receipt.getPaymentDate())), regularFont));

            detailsTable.addCell(getBorderedCell("Name:", boldFont));
            detailsTable.addCell(getBorderedCell(getValueOrDefault(student.getFirstName()+" "+student.getSurname()), regularFont));
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
            detailsTable.addCell(getBorderedCell(getValueOrDefault(payment.getInstallments()+" Installments"), regularFont));
            detailsTable.addCell(getBorderedCell("Due Date:", boldFont));
            detailsTable.addCell(getBorderedCell(getValueOrDefault(payment.getDueDate()), regularFont));

            outerTable.addCell(new Cell().add(detailsTable).setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

            // **Fee Details Table**
            Table feeTable = new Table(new float[]{1, 5, 3, 1}).useAllAvailableWidth();
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

            outerTable.addCell(new Cell().add(feeTable).setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

            outerTable.addCell(new Cell().add(new Paragraph("Amount (In word) "+NumberToWordConverter.convert(receipt.getAmountPaid())+"  ONLY").setFont(boldFont))
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

            // **Signatures Section**
            Table signTable = new Table(new float[]{1, 1, 1}).useAllAvailableWidth();
            signTable.addCell(getSignatureCell("Clerk"));
            signTable.addCell(getSignatureCell("Cashier"));
            signTable.addCell(getSignatureCell("Accountant"));

            outerTable.addCell(new Cell().add(signTable).setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

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
        table.addCell(getBorderedCell1(srNo, PdfFontFactory.createFont(StandardFonts.HELVETICA))).setBorder(null);
        table.addCell(getBorderedCell1(particulars, PdfFontFactory.createFont(StandardFonts.HELVETICA))).setBorder(null);
        table.addCell(getBorderedCell1(amount, PdfFontFactory.createFont(StandardFonts.HELVETICA))).setBorder(null);
        table.addCell(getBorderedCell1("", PdfFontFactory.createFont(StandardFonts.HELVETICA))).setBorder(null);
    }

    // private static void addFeeRow(Table table, String srNo, String particulars, String amount) throws IOException {
    //     table.addCell(getBorderedCell(srNo, PdfFontFactory.createFont(StandardFonts.HELVETICA)));
    //     table.addCell(getBorderedCell(particulars, PdfFontFactory.createFont(StandardFonts.HELVETICA)));
    //     table.addCell(getBorderedCell(amount, PdfFontFactory.createFont(StandardFonts.HELVETICA)));
    //     table.addCell(getBorderedCell("", PdfFontFactory.createFont(StandardFonts.HELVETICA)));
    // }

    // Helper method to create signature cell
    private static Cell getSignatureCell(String text) throws IOException {
        return new Cell().add(new Paragraph(text).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10)
                .setBorder(Border.NO_BORDER);
    }
}

