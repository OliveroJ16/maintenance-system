package com.maintenancesystem.maintenanceSystem.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.entity.Service;
import com.maintenancesystem.maintenanceSystem.pdf.PDFGeneratorService;
import com.maintenancesystem.maintenanceSystem.service.ReportDataService;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CostReportService {

    private final ReportDataService reportDataService;
    private final PDFGeneratorService pdfGenerator;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ByteArrayInputStream generateCostsPDF(LocalDate startDate, LocalDate endDate, Integer workshopId) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            pdfGenerator.addTitle(document, "Reporte de Costos de Mantenimiento");

            String dateRange = "";
            if (startDate != null && endDate != null) {
                dateRange = String.format("Período: %s - %s",
                        startDate.format(DATE_FORMATTER), endDate.format(DATE_FORMATTER));
            }
            pdfGenerator.addSubtitle(document, dateRange.isEmpty() ?
                    "Generado el: " + LocalDate.now().format(DATE_FORMATTER) : dateRange);

            List<Maintenance> maintenances = reportDataService.filterMaintenances(startDate, endDate, null, null, workshopId);
            Map<String, List<Maintenance>> byWorkshop = reportDataService.groupMaintenancesByWorkshop(maintenances);

            BigDecimal totalCost = BigDecimal.ZERO;

            for (Map.Entry<String, List<Maintenance>> entry : byWorkshop.entrySet()) {
                pdfGenerator.addSectionTitle(document, entry.getKey(), BaseColor.BLACK, 20, 10);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1.5f, 2.5f, 2f, 1.5f, 1.5f});

                pdfGenerator.addTableHeader(table, new String[]{
                        "Vehículo", "Tipo Mantenimiento", "Fecha", "Servicios", "Costo"
                });

                BigDecimal workshopTotal = BigDecimal.ZERO;

                for (Maintenance m : entry.getValue()) {
                    List<com.maintenancesystem.maintenanceSystem.entity.Service> services = reportDataService.getServicesForMaintenance(
                            m.getIdMaintenance(), m.getWorkshop().getIdWorkshop());

                    BigDecimal maintenanceCost = reportDataService.calculateMaintenanceCost(m);
                    workshopTotal = workshopTotal.add(maintenanceCost);

                    table.addCell(pdfGenerator.createCell(m.getVehicle().getPlate()));
                    table.addCell(pdfGenerator.createCell(m.getMaintenanceType().getTypeName()));
                    table.addCell(pdfGenerator.createCell(m.getExecutionDate() != null ?
                            m.getExecutionDate().format(DATE_FORMATTER) :
                            m.getScheduledDate().format(DATE_FORMATTER)));
                    table.addCell(pdfGenerator.createCell(services.size() + " servicio(s)"));
                    table.addCell(pdfGenerator.createCell("$" + String.format("%,.2f", maintenanceCost)));
                }

                // Fila de subtotal
                table.addCell(pdfGenerator.createColspanCell("Subtotal " + entry.getKey(),
                        4, Element.ALIGN_RIGHT, new BaseColor(240, 240, 240)));
                table.addCell(pdfGenerator.createFormattedCell("$" + String.format("%,.2f", workshopTotal),
                        Element.ALIGN_LEFT, new BaseColor(240, 240, 240)));

                document.add(table);
                totalCost = totalCost.add(workshopTotal);
            }

            pdfGenerator.addTotalParagraph(document,
                    String.format("TOTAL GENERAL: $%,.2f", totalCost), 16, 30);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}