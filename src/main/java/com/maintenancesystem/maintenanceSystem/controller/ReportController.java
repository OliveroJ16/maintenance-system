package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    // ========== VEHÍCULOS Y ASIGNACIONES ==========

    @PostMapping("/vehicles-assignments/pdf")
    public ResponseEntity<InputStreamResource> vehiclesAssignmentsReport(
            @RequestParam(required = false) String status) {

        ByteArrayInputStream pdf = reportService.generateVehiclesAssignmentsPDF(status);

        return buildPdfResponse(pdf, "reporte_vehiculos_asignaciones.pdf");
    }

    // ========== MANTENIMIENTOS ==========

    @PostMapping("/maintenances/pdf")
    public ResponseEntity<InputStreamResource> maintenancesReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer vehicleId) {

        ByteArrayInputStream pdf =
                reportService.generateMaintenancesPDF(startDate, endDate, status, vehicleId);

        return buildPdfResponse(pdf, "reporte_mantenimientos.pdf");
    }

    // ========== COSTOS ==========

    @PostMapping("/costs/pdf")
    public ResponseEntity<InputStreamResource> costsReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @RequestParam(required = false) Integer workshopId) {

        ByteArrayInputStream pdf =
                reportService.generateCostsPDF(startDate, endDate, workshopId);

        return buildPdfResponse(pdf, "reporte_costos.pdf");
    }

    // ========== ESTADÍSTICAS ==========

    @PostMapping("/statistics/pdf")
    public ResponseEntity<InputStreamResource> statisticsReport() {

        ByteArrayInputStream pdf = reportService.generateStatisticsPDF();

        return buildPdfResponse(pdf, "reporte_estadisticas.pdf");
    }


    private ResponseEntity<InputStreamResource> buildPdfResponse(
            ByteArrayInputStream pdf,
            String filename) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment().filename(filename).build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}