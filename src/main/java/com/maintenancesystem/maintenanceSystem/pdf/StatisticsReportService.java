package com.maintenancesystem.maintenanceSystem.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.maintenancesystem.maintenanceSystem.entity.*;
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
public class StatisticsReportService {

    private final ReportDataService reportDataService;
    private final PDFGeneratorService pdfGenerator;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ByteArrayInputStream generateStatisticsPDF() {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            pdfGenerator.addTitle(document, "Estadísticas Generales del Sistema");
            pdfGenerator.addSubtitle(document, "Generado el: " + LocalDate.now().format(DATE_FORMATTER));

            // Sección de Vehículos
            pdfGenerator.addSectionTitle(document, "VEHÍCULOS", BaseColor.BLUE, 20, 10);

            List<Vehicle> vehicles = reportDataService.getAllVehicles();
            Map<String, Long> vehicleStats = reportDataService.getVehicleStatistics(vehicles);

            pdfGenerator.addStatLine(document, "Total de vehículos:", String.valueOf(vehicleStats.get("total")));
            pdfGenerator.addStatLine(document, "Vehículos activos:", String.valueOf(vehicleStats.get("active")));
            pdfGenerator.addStatLine(document, "Vehículos inactivos:", String.valueOf(vehicleStats.get("inactive")));
            pdfGenerator.addStatLine(document, "En mantenimiento:", String.valueOf(vehicleStats.get("maintenance")));
            pdfGenerator.addStatLine(document, "Kilometraje total de flota:",
                    String.format("%,d km", vehicleStats.get("totalMileage")));

            // Sección de Mantenimientos
            pdfGenerator.addSectionTitle(document, "MANTENIMIENTOS", BaseColor.BLUE, 20, 10);

            List<Maintenance> maintenances = reportDataService.filterMaintenances(null, null, null, null);
            Map<String, Long> maintStats = reportDataService.getMaintenanceStatistics(maintenances);

            pdfGenerator.addStatLine(document, "Total de mantenimientos:", String.valueOf(maintStats.get("total")));
            pdfGenerator.addStatLine(document, "Completados:", String.valueOf(maintStats.get("completed")));
            pdfGenerator.addStatLine(document, "Pendientes:", String.valueOf(maintStats.get("pending")));
            pdfGenerator.addStatLine(document, "En proceso:", String.valueOf(maintStats.get("inProcess")));

            // Mantenimientos por tipo
            pdfGenerator.addSectionTitle(document, "Mantenimientos por tipo:", BaseColor.BLACK, 15, 5);

            Map<String, Long> byType = reportDataService.getMaintenancesByType(maintenances);
            for (Map.Entry<String, Long> entry : byType.entrySet()) {
                pdfGenerator.addStatLine(document, "  • " + entry.getKey() + ":", String.valueOf(entry.getValue()));
            }

            // Sección de Talleres
            pdfGenerator.addSectionTitle(document, "TALLERES", BaseColor.BLUE, 20, 10);

            List<Workshop> workshops = reportDataService.getAllWorkshops();
            pdfGenerator.addStatLine(document, "Total de talleres registrados:", String.valueOf(workshops.size()));

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}