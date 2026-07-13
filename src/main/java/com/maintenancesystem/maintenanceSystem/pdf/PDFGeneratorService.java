package com.maintenancesystem.maintenanceSystem.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class PDFGeneratorService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void addTitle(Document document, String title) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph titleParagraph = new Paragraph(title, titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(10);
        document.add(titleParagraph);
    }

    public void addSubtitle(Document document, String subtitle) throws DocumentException {
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
        Paragraph subtitleParagraph = new Paragraph(subtitle, subtitleFont);
        subtitleParagraph.setAlignment(Element.ALIGN_CENTER);
        subtitleParagraph.setSpacingAfter(20);
        document.add(subtitleParagraph);
    }

    public void addTableHeader(PdfPTable table, String[] headers) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(new BaseColor(51, 51, 51));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            table.addCell(cell);
        }
    }

    public PdfPCell createCell(String content) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
        PdfPCell cell = new PdfPCell(new Phrase(content, cellFont));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    public PdfPCell createFormattedCell(String content, int alignment, BaseColor backgroundColor) {
        PdfPCell cell = createCell(content);
        cell.setHorizontalAlignment(alignment);
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        return cell;
    }

    public PdfPCell createColspanCell(String content, int colspan, int alignment, BaseColor backgroundColor) {
        PdfPCell cell = createCell(content);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        return cell;
    }

    public void addStatLine(Document document, String label, String value) throws DocumentException {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

        Paragraph line = new Paragraph();
        line.add(new Chunk(label + " ", labelFont));
        line.add(new Chunk(value, valueFont));
        line.setSpacingAfter(5);
        document.add(line);
    }

    public void addSectionTitle(Document document, String title, BaseColor color, int spacingBefore, int spacingAfter)
            throws DocumentException {
        Paragraph sectionTitle = new Paragraph(title,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, color));
        sectionTitle.setSpacingBefore(spacingBefore);
        sectionTitle.setSpacingAfter(spacingAfter);
        document.add(sectionTitle);
    }

    public void addTotalParagraph(Document document, String text, int fontSize, int spacingBefore)
            throws DocumentException {
        Paragraph totalParagraph = new Paragraph(text,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, fontSize, BaseColor.BLACK));
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        totalParagraph.setSpacingBefore(spacingBefore);
        document.add(totalParagraph);
    }

    public void addStatisticsSummary(Document document, Map<String, Long> stats, String format)
            throws DocumentException {
        Font statsFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        Paragraph statsParagraph = new Paragraph();
        statsParagraph.setFont(statsFont);
        statsParagraph.setSpacingBefore(10);
        statsParagraph.setSpacingAfter(15);
        statsParagraph.add(String.format(format,
                stats.get("total"), stats.get("active"), stats.get("inactive")));
        document.add(statsParagraph);
    }

    public PdfPCell createStatusCell(String status) {
        PdfPCell statusCell = createCell(status);
        if (status.equals("ACTIVO") || status.equals("COMPLETADO")) {
            statusCell.setBackgroundColor(new BaseColor(220, 252, 231));
        } else if (status.equals("MANTENIMIENTO") || status.equals("PENDIENTE")) {
            statusCell.setBackgroundColor(new BaseColor(254, 249, 195));
        }
        return statusCell;
    }
}