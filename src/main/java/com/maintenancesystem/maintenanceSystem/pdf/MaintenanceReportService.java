package com.maintenancesystem.maintenanceSystem.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.enums.MaintenanceStatus;
import com.maintenancesystem.maintenanceSystem.pdf.PDFGeneratorService;
import com.maintenancesystem.maintenanceSystem.service.ReportDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MaintenanceReportService {

    private final ReportDataService reportDataService;
    private final PDFGeneratorService pdfGenerator;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ByteArrayInputStream generateMaintenancesPDF(LocalDate startDate, LocalDate endDate,
                                                        MaintenanceStatus status, Integer vehicleId) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            pdfGenerator.addTitle(document, "Reporte de Mantenimientos");

            String dateRange = "";
            if (startDate != null && endDate != null) {
                dateRange = String.format("Período: %s - %s",
                        startDate.format(DATE_FORMATTER), endDate.format(DATE_FORMATTER));
            }
            pdfGenerator.addSubtitle(document, dateRange.isEmpty() ?
                    "Generado el: " + LocalDate.now().format(DATE_FORMATTER) : dateRange);

            List<Maintenance> maintenances = reportDataService.filterMaintenances(startDate, endDate, status, vehicleId);
            Map<String, Long> mainStats = reportDataService.getMaintenanceStatistics(maintenances);

            pdfGenerator.addStatisticsSummary(document, mainStats, "Total: %d | Completados: %d | Pendientes: %d");

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setWidths(new float[]{0.8f, 1.5f, 2f, 1.5f, 1.5f, 1.2f, 1.5f, 2f});

            pdfGenerator.addTableHeader(table, new String[]{
                    "ID", "Vehículo", "Tipo Mant.", "Fecha Prog.", "Fecha Ejec.", "KM", "Estado", "Taller"
            });

            for (Maintenance m : maintenances) {
                table.addCell(pdfGenerator.createCell(String.valueOf(m.getIdMaintenance())));
                table.addCell(pdfGenerator.createCell(m.getVehicle().getPlate() + "\n" +
                        m.getVehicle().getBrand() + " " + m.getVehicle().getModel()));
                table.addCell(pdfGenerator.createCell(m.getMaintenanceType().getTypeName()));
                table.addCell(pdfGenerator.createCell(m.getScheduledDate().format(DATE_FORMATTER)));
                table.addCell(pdfGenerator.createCell(m.getExecutionDate() != null ?
                        m.getExecutionDate().format(DATE_FORMATTER) : "Pendiente"));
                table.addCell(pdfGenerator.createCell(String.format("%,d", m.getKilometers())));
                table.addCell(pdfGenerator.createStatusCell(m.getStatus().name()));
                table.addCell(pdfGenerator.createCell(m.getWorkshop() != null ?
                        m.getWorkshop().getWorkshopName() : "N/A"));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}