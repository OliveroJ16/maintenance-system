package com.maintenancesystem.maintenanceSystem.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.maintenancesystem.maintenanceSystem.entity.*;
import com.maintenancesystem.maintenanceSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    private final WorkshopRepository workshopRepository;
    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final VehicleAssignmentRepository vehicleAssignmentRepository;
    private final ServiceRepository serviceRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ========== MÉTODOS DE CONSULTA ==========

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<MaintenanceType> getAllMaintenanceTypes() {
        return maintenanceTypeRepository.findAll();
    }

    public List<Workshop> getAllWorkshops() {
        return workshopRepository.findAll();
    }

    public long getTotalVehicles() {
        return vehicleRepository.count();
    }

    public long getActiveVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getStatus().name().equals("ACTIVO"))
                .count();
    }

    public long getTotalMaintenances() {
        return maintenanceRepository.count();
    }

    public long getCompletedMaintenances() {
        return maintenanceRepository.findAll().stream()
                .filter(m -> m.getStatus().name().equals("COMPLETADO"))
                .count();
    }

    // ========== REPORTE DE VEHÍCULOS Y ASIGNACIONES ==========

    public ByteArrayInputStream generateVehiclesAssignmentsPDF(String status) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            addTitle(document, "Reporte de Vehículos y Asignaciones");
            addSubtitle(document, "Generado el: " + LocalDate.now().format(DATE_FORMATTER));

            // Filtrar vehículos por estado si se especifica
            List<Vehicle> vehicles = vehicleRepository.findAll();
            if (status != null && !status.isEmpty()) {
                vehicles = vehicles.stream()
                        .filter(v -> v.getStatus().name().equals(status))
                        .collect(Collectors.toList());
            }

            // Estadísticas resumidas
            addStatisticsParagraph(document, vehicles);

            // Tabla de vehículos
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setWidths(new float[]{1.5f, 1.5f, 1.5f, 1.2f, 1.5f, 2f, 1.2f, 1.2f});

            addTableHeader(table, new String[]{
                    "Placa", "Marca", "Modelo", "Tipo", "Kilometraje", "Conductor Asignado", "Combustible", "Estado"
            });

            for (Vehicle v : vehicles) {
                table.addCell(createCell(v.getPlate()));
                table.addCell(createCell(v.getBrand()));
                table.addCell(createCell(v.getModel()));
                table.addCell(createCell(v.getVehicleType().name()));
                table.addCell(createCell(String.format("%,d km", v.getMileage())));

                // Obtener conductor asignado
                String driver = getDriverForVehicle(v.getIdVehicle());
                table.addCell(createCell(driver));

                table.addCell(createCell(v.getFuelType().name()));

                PdfPCell statusCell = createCell(v.getStatus().name());
                if (v.getStatus().name().equals("ACTIVO")) {
                    statusCell.setBackgroundColor(new BaseColor(220, 252, 231));
                } else if (v.getStatus().name().equals("MANTENIMIENTO")) {
                    statusCell.setBackgroundColor(new BaseColor(254, 249, 195));
                }
                table.addCell(statusCell);
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // ========== REPORTE DE MANTENIMIENTOS ==========

    public ByteArrayInputStream generateMaintenancesPDF(LocalDate startDate, LocalDate endDate,
                                                        String status, Integer vehicleId) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            addTitle(document, "Reporte de Mantenimientos");

            String dateRange = "";
            if (startDate != null && endDate != null) {
                dateRange = String.format("Período: %s - %s",
                        startDate.format(DATE_FORMATTER), endDate.format(DATE_FORMATTER));
            }
            addSubtitle(document, dateRange.isEmpty() ?
                    "Generado el: " + LocalDate.now().format(DATE_FORMATTER) : dateRange);

            // Filtrar mantenimientos
            List<Maintenance> maintenances = filterMaintenances(startDate, endDate, status, vehicleId);

            // Estadísticas
            addMaintenanceStats(document, maintenances);

            // Tabla
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setWidths(new float[]{0.8f, 1.5f, 2f, 1.5f, 1.5f, 1.2f, 1.5f, 2f});

            addTableHeader(table, new String[]{
                    "ID", "Vehículo", "Tipo Mant.", "Fecha Prog.", "Fecha Ejec.", "KM", "Estado", "Taller"
            });

            for (Maintenance m : maintenances) {
                table.addCell(createCell(String.valueOf(m.getIdMaintenance())));
                table.addCell(createCell(m.getVehicle().getPlate() + "\n" +
                        m.getVehicle().getBrand() + " " + m.getVehicle().getModel()));
                table.addCell(createCell(m.getMaintenanceType().getTypeName()));
                table.addCell(createCell(m.getScheduledDate().format(DATE_FORMATTER)));
                table.addCell(createCell(m.getExecutionDate() != null ?
                        m.getExecutionDate().format(DATE_FORMATTER) : "Pendiente"));
                table.addCell(createCell(String.format("%,d", m.getKilometers())));

                PdfPCell statusCell = createCell(m.getStatus().name());
                if (m.getStatus().name().equals("COMPLETADO")) {
                    statusCell.setBackgroundColor(new BaseColor(220, 252, 231));
                } else if (m.getStatus().name().equals("PENDIENTE")) {
                    statusCell.setBackgroundColor(new BaseColor(254, 249, 195));
                }
                table.addCell(statusCell);

                table.addCell(createCell(m.getWorkshop() != null ?
                        m.getWorkshop().getWorkshopName() : "N/A"));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // ========== REPORTE DE COSTOS ==========

    public ByteArrayInputStream generateCostsPDF(LocalDate startDate, LocalDate endDate, Integer workshopId) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            addTitle(document, "Reporte de Costos de Mantenimiento");

            String dateRange = "";
            if (startDate != null && endDate != null) {
                dateRange = String.format("Período: %s - %s",
                        startDate.format(DATE_FORMATTER), endDate.format(DATE_FORMATTER));
            }
            addSubtitle(document, dateRange.isEmpty() ?
                    "Generado el: " + LocalDate.now().format(DATE_FORMATTER) : dateRange);

            // Obtener mantenimientos y servicios
            List<Maintenance> maintenances = filterMaintenances(startDate, endDate, null, null);

            if (workshopId != null) {
                maintenances = maintenances.stream()
                        .filter(m -> m.getWorkshop() != null && m.getWorkshop().getIdWorkshop().equals(workshopId))
                        .collect(Collectors.toList());
            }

            // Agrupar por taller
            Map<String, List<Maintenance>> byWorkshop = maintenances.stream()
                    .filter(m -> m.getWorkshop() != null)
                    .collect(Collectors.groupingBy(m -> m.getWorkshop().getWorkshopName()));

            // Calcular costos por taller
            BigDecimal totalCost = BigDecimal.ZERO;

            for (Map.Entry<String, List<Maintenance>> entry : byWorkshop.entrySet()) {
                Paragraph workshopTitle = new Paragraph(entry.getKey(),
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
                workshopTitle.setSpacingBefore(20);
                workshopTitle.setSpacingAfter(10);
                document.add(workshopTitle);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1.5f, 2.5f, 2f, 1.5f, 1.5f});

                addTableHeader(table, new String[]{
                        "Vehículo", "Tipo Mantenimiento", "Fecha", "Servicios", "Costo"
                });

                BigDecimal workshopTotal = BigDecimal.ZERO;

                for (Maintenance m : entry.getValue()) {
                    // Obtener servicios asociados al mantenimiento
                    List<com.maintenancesystem.maintenanceSystem.entity.Service> services =
                            getServicesForMaintenance(m.getIdMaintenance(), m.getWorkshop().getIdWorkshop());

                    BigDecimal maintenanceCost = services.stream()
                            .map(com.maintenancesystem.maintenanceSystem.entity.Service::getCost)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    workshopTotal = workshopTotal.add(maintenanceCost);

                    table.addCell(createCell(m.getVehicle().getPlate()));
                    table.addCell(createCell(m.getMaintenanceType().getTypeName()));
                    table.addCell(createCell(m.getExecutionDate() != null ?
                            m.getExecutionDate().format(DATE_FORMATTER) :
                            m.getScheduledDate().format(DATE_FORMATTER)));
                    table.addCell(createCell(services.size() + " servicio(s)"));
                    table.addCell(createCell("$" + String.format("%,.2f", maintenanceCost)));
                }

                // Fila de subtotal
                PdfPCell subtotalLabel = createCell("Subtotal " + entry.getKey());
                subtotalLabel.setColspan(4);
                subtotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subtotalLabel.setBackgroundColor(new BaseColor(240, 240, 240));
                table.addCell(subtotalLabel);

                PdfPCell subtotalValue = createCell("$" + String.format("%,.2f", workshopTotal));
                subtotalValue.setBackgroundColor(new BaseColor(240, 240, 240));
                table.addCell(subtotalValue);

                document.add(table);
                totalCost = totalCost.add(workshopTotal);
            }

            // Total general
            Paragraph totalParagraph = new Paragraph(
                    String.format("TOTAL GENERAL: $%,.2f", totalCost),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)
            );
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            totalParagraph.setSpacingBefore(30);
            document.add(totalParagraph);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // ========== REPORTE DE ESTADÍSTICAS GENERALES ==========

    public ByteArrayInputStream generateStatisticsPDF() {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            addTitle(document, "Estadísticas Generales del Sistema");
            addSubtitle(document, "Generado el: " + LocalDate.now().format(DATE_FORMATTER));

            // Estadísticas de vehículos
            Paragraph vehiclesTitle = new Paragraph("VEHÍCULOS",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLUE));
            vehiclesTitle.setSpacingBefore(20);
            vehiclesTitle.setSpacingAfter(10);
            document.add(vehiclesTitle);

            List<Vehicle> vehicles = vehicleRepository.findAll();
            long active = vehicles.stream().filter(v -> v.getStatus().name().equals("ACTIVO")).count();
            long inactive = vehicles.stream().filter(v -> v.getStatus().name().equals("INACTIVO")).count();
            long maintenance = vehicles.stream().filter(v -> v.getStatus().name().equals("MANTENIMIENTO")).count();
            int totalKm = vehicles.stream().mapToInt(Vehicle::getMileage).sum();

            addStatLine(document, "Total de vehículos:", String.valueOf(vehicles.size()));
            addStatLine(document, "Vehículos activos:", String.valueOf(active));
            addStatLine(document, "Vehículos inactivos:", String.valueOf(inactive));
            addStatLine(document, "En mantenimiento:", String.valueOf(maintenance));
            addStatLine(document, "Kilometraje total de flota:", String.format("%,d km", totalKm));

            // Estadísticas de mantenimientos
            Paragraph maintenanceTitle = new Paragraph("MANTENIMIENTOS",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLUE));
            maintenanceTitle.setSpacingBefore(20);
            maintenanceTitle.setSpacingAfter(10);
            document.add(maintenanceTitle);

            List<Maintenance> maintenances = maintenanceRepository.findAll();
            long completed = maintenances.stream().filter(m -> m.getStatus().name().equals("COMPLETADO")).count();
            long pending = maintenances.stream().filter(m -> m.getStatus().name().equals("PENDIENTE")).count();
            long inProcess = maintenances.stream().filter(m -> m.getStatus().name().equals("EN_PROCESO")).count();

            addStatLine(document, "Total de mantenimientos:", String.valueOf(maintenances.size()));
            addStatLine(document, "Completados:", String.valueOf(completed));
            addStatLine(document, "Pendientes:", String.valueOf(pending));
            addStatLine(document, "En proceso:", String.valueOf(inProcess));

            // Mantenimientos por tipo
            Map<String, Long> byType = maintenances.stream()
                    .collect(Collectors.groupingBy(
                            m -> m.getMaintenanceType().getTypeName(),
                            Collectors.counting()
                    ));

            Paragraph typeTitle = new Paragraph("Mantenimientos por tipo:",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            typeTitle.setSpacingBefore(15);
            typeTitle.setSpacingAfter(5);
            document.add(typeTitle);

            for (Map.Entry<String, Long> entry : byType.entrySet()) {
                addStatLine(document, "  • " + entry.getKey() + ":", String.valueOf(entry.getValue()));
            }

            // Estadísticas de talleres
            Paragraph workshopsTitle = new Paragraph("TALLERES",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLUE));
            workshopsTitle.setSpacingBefore(20);
            workshopsTitle.setSpacingAfter(10);
            document.add(workshopsTitle);

            List<Workshop> workshops = workshopRepository.findAll();
            addStatLine(document, "Total de talleres registrados:", String.valueOf(workshops.size()));

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // ========== MÉTODOS AUXILIARES ==========

    private List<Maintenance> filterMaintenances(LocalDate startDate, LocalDate endDate,
                                                 String status, Integer vehicleId) {
        List<Maintenance> maintenances = maintenanceRepository.findAll();

        if (startDate != null) {
            maintenances = maintenances.stream()
                    .filter(m -> !m.getScheduledDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        if (endDate != null) {
            maintenances = maintenances.stream()
                    .filter(m -> !m.getScheduledDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        if (status != null && !status.isEmpty()) {
            maintenances = maintenances.stream()
                    .filter(m -> m.getStatus().name().equals(status))
                    .collect(Collectors.toList());
        }

        if (vehicleId != null) {
            maintenances = maintenances.stream()
                    .filter(m -> m.getVehicle().getIdVehicle().equals(vehicleId))
                    .collect(Collectors.toList());
        }

        return maintenances;
    }

    private String getDriverForVehicle(Integer vehicleId) {
        List<VehicleAssignment> assignments = vehicleAssignmentRepository.findAll().stream()
                .filter(a -> a.getVehicle().getIdVehicle().equals(vehicleId))
                .collect(Collectors.toList());

        if (assignments.isEmpty()) {
            return "Sin asignar";
        }

        VehicleAssignment latest = assignments.stream()
                .max(Comparator.comparing(VehicleAssignment::getAssignmentDate))
                .orElse(null);

        if (latest != null && latest.getDriver() != null) {
            return latest.getDriver().getFirstName() + " " + latest.getDriver().getLastName();
        }

        return "Sin asignar";
    }

    private List<com.maintenancesystem.maintenanceSystem.entity.Service> getServicesForMaintenance(
            Integer maintenanceId, Integer workshopId) {
        // Retornar servicios del taller (simulado por ahora)
        // En una implementación real, necesitarías una tabla de relación MaintenanceService
        return serviceRepository.findAll().stream()
                .filter(s -> s.getWorkshop().getIdWorkshop().equals(workshopId))
                .limit(2) // Simulación: 2 servicios por mantenimiento
                .collect(Collectors.toList());
    }

    // Métodos de formato PDF

    private void addTitle(Document document, String title) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph titleParagraph = new Paragraph(title, titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(10);
        document.add(titleParagraph);
    }

    private void addSubtitle(Document document, String subtitle) throws DocumentException {
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
        Paragraph subtitleParagraph = new Paragraph(subtitle, subtitleFont);
        subtitleParagraph.setAlignment(Element.ALIGN_CENTER);
        subtitleParagraph.setSpacingAfter(20);
        document.add(subtitleParagraph);
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
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

    private PdfPCell createCell(String content) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
        PdfPCell cell = new PdfPCell(new Phrase(content, cellFont));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private void addStatisticsParagraph(Document document, List<Vehicle> vehicles) throws DocumentException {
        long active = vehicles.stream().filter(v -> v.getStatus().name().equals("ACTIVO")).count();

        Font statsFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        Paragraph stats = new Paragraph();
        stats.setFont(statsFont);
        stats.setSpacingBefore(10);
        stats.setSpacingAfter(15);
        stats.add(String.format("Total de vehículos: %d | Activos: %d | Inactivos: %d",
                vehicles.size(), active, vehicles.size() - active));
        document.add(stats);
    }

    private void addMaintenanceStats(Document document, List<Maintenance> maintenances) throws DocumentException {
        long completed = maintenances.stream().filter(m -> m.getStatus().name().equals("COMPLETADO")).count();
        long pending = maintenances.stream().filter(m -> m.getStatus().name().equals("PENDIENTE")).count();

        Font statsFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        Paragraph stats = new Paragraph();
        stats.setFont(statsFont);
        stats.setSpacingBefore(10);
        stats.setSpacingAfter(15);
        stats.add(String.format("Total: %d | Completados: %d | Pendientes: %d",
                maintenances.size(), completed, pending));
        document.add(stats);
    }

    private void addStatLine(Document document, String label, String value) throws DocumentException {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

        Paragraph line = new Paragraph();
        line.add(new Chunk(label + " ", labelFont));
        line.add(new Chunk(value, valueFont));
        line.setSpacingAfter(5);
        document.add(line);
    }
}