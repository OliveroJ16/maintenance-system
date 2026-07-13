package com.maintenancesystem.maintenanceSystem.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.enums.VehicleStatus;
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
public class VehicleReportService {

    private final ReportDataService reportDataService;
    private final PDFGeneratorService pdfGenerator;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ByteArrayInputStream generateVehiclesAssignmentsPDF(VehicleStatus status) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            pdfGenerator.addTitle(document, "Reporte de Vehículos y Asignaciones");
            pdfGenerator.addSubtitle(document, "Generado el: " + LocalDate.now().format(DATE_FORMATTER));

            List<Vehicle> vehicles = reportDataService.filterVehiclesByStatus(status);
            Map<String, Long> vehicleStats = reportDataService.getVehicleStatistics(vehicles);

            pdfGenerator.addStatisticsSummary(document, vehicleStats,
                    "Total de vehículos: %d | Activos: %d | Inactivos: %d");

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setWidths(new float[]{1.5f, 1.5f, 1.5f, 1.2f, 1.5f, 2f, 1.2f, 1.2f});

            pdfGenerator.addTableHeader(table, new String[]{
                    "Placa", "Marca", "Modelo", "Tipo", "Kilometraje", "Conductor Asignado", "Combustible", "Estado"
            });

            for (Vehicle v : vehicles) {
                table.addCell(pdfGenerator.createCell(v.getPlate()));
                table.addCell(pdfGenerator.createCell(v.getBrand()));
                table.addCell(pdfGenerator.createCell(v.getModel()));
                table.addCell(pdfGenerator.createCell(v.getVehicleType().name()));
                table.addCell(pdfGenerator.createCell(String.format("%,d km", v.getMileage())));

                String driver = reportDataService.getDriverForVehicle(v.getIdVehicle());
                table.addCell(pdfGenerator.createCell(driver));

                table.addCell(pdfGenerator.createCell(v.getFuelType().name()));
                table.addCell(pdfGenerator.createStatusCell(v.getStatus().name()));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}