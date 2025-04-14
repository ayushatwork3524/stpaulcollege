package com.main.stpaul.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.main.stpaul.services.serviceInterface.TCService;

@Service
public class TCServiceImpl implements TCService {

    @Override
    public ByteArrayOutputStream generateTc(String studentId) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Set font
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Header - School Name
        Paragraph header = new Paragraph("ST. PAUL SENIOR SECONDARY SCHOOL")
                .setFont(bold)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER);
        doc.add(header);

        doc.add(new Paragraph("Affiliated to Central Board of Secondary Education, New Delhi (Affiliation No: 1130345)")
                .setFont(font)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("Near Bridge, Hudkeshwar Road, Nagpur-440034 (Maharashtra)")
                .setFont(font)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("Email - info@stpaulnagpur.com   Website - www.stpaulschool.ac.in")
                .setFont(font)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("UDISE CODE :- 27090908705")
                .setFont(bold)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("\nTRANSFER CERTIFICATE")
                .setFont(bold)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setUnderline());

        doc.add(new Paragraph("\n"));

        // Start of content
        addEntry(doc, bold, font, "TC No.", "1014", "Admission No.", "1026");
        addEntry(doc, bold, font, "Student (Portal) ID No.", "2010270903107100026", "UID Aadhar Card No.",
                "2733 2028 1358");
        addEntry(doc, bold, font, "1. Name of the pupil", "PRAJAKTA DURGE");
        addEntry(doc, bold, font, "2. Father's Name/Guardian's Name", "CHANDU DURGE");
        addEntry(doc, bold, font, "3. Mother's Name", "SUNITA DURGE");
        addEntry(doc, bold, font, "4. Nationality", "INDIAN", "Mother Tongue", "MARATHI");
        addEntry(doc, bold, font, "5. Religion", "HINDU", "Category", "OBC");
        addEntry(doc, bold, font, "   ", "", "Caste", "WANI");
        addEntry(doc, bold, font, "6. Date of first admission in the school with class", "22/07/2022", "in", "STD. XI");
        addEntry(doc, bold, font, "7. Date of Birth (In Christian Era)", "30/06/2007", "Place of Birth",
                "KALMESHWAR, NAG");
        addEntry(doc, bold, font, "   Admission Register (in figures)", "", "", "");
        addEntry(doc, bold, font, "   (In Words)", "THIRTIETH JUNE TWO THOUSAND SEVEN");

        // More entries...
        addEntry(doc, bold, font, "8. Class in which the pupil last studied / is studying", "XIIth", "(In Words)",
                "TWELFTH");
        addEntry(doc, bold, font, "9. School / Board Annual Examination last taken",
                "AISSCE (XII) 2024, CBSE, NEW DELHI");
        addEntry(doc, bold, font, "10. Whether qualified for Promotion of higher class", "YES");
        addEntry(doc, bold, font, "   If so, to which class", "HIGHER STUDIES");
        addEntry(doc, bold, font, "11. Subject Studied", "ENGLISH CORE, PHYSICS, CHEMISTRY, BIOLOGY, COMPUTER SCIENCE");
        addEntry(doc, bold, font, "12. Month upto which the pupil has paid school dues", "ALL DUES PAID");
        addEntry(doc, bold, font, "13. Any fee Concession availed of", "N.A");
        addEntry(doc, bold, font, "14. Total No. of working days", "220");
        addEntry(doc, bold, font, "15. Total No. of working days when the pupil was present", "205");
        addEntry(doc, bold, font, "16. Whether NCC Cadet/ Boys Scout/ Girls Guide", "N.A");
        addEntry(doc, bold, font, "17. Games played / extra-curricular activities", "N.A");
        addEntry(doc, bold, font, "18. General Conduct", "GOOD");
        addEntry(doc, bold, font, "19. Date of application for certificate", "13/05/2024");
        addEntry(doc, bold, font, "20. Date of issue for certificate", "13/05/2024");
        addEntry(doc, bold, font, "21. Reason for leaving the school", "TERM IS OVER");
        addEntry(doc, bold, font, "22. Any other remarks", "SHE HAS PASSED AISSCE (XII) EXAM - 2024");

        // Certification & Signature section
        doc.add(new Paragraph(
                "\nCertified that the above information is in accordance with the school Admission Register.")
                .setFont(font)
                .setFontSize(9));

        doc.add(new Paragraph("\n\nPRINCIPAL")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFont(bold));

        doc.close();

        return baos;
    }

    private void addEntry(Document doc, PdfFont bold, PdfFont font, String label1, String value1) {
        addEntry(doc, bold, font, label1, value1, null, null);
    }

    private void addEntry(Document doc, PdfFont bold, PdfFont font, String label1, String value1, String label2,
            String value2) {
        Paragraph p = new Paragraph()
                .setFont(font)
                .setFontSize(10)
                .setMarginBottom(2);
        p.add(new Text(label1 + " ").setFont(bold));
        p.add(new Text(value1).setUnderline());
        if (label2 != null) {
            p.add(new Text("   " + label2 + " ").setFont(bold));
            p.add(new Text(value2).setUnderline());
        }
        doc.add(p);
    }

}
