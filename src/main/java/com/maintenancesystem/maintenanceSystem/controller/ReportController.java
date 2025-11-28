package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public String reportsPage(Model model) {
        // Cargar datos para la página de reportes
        model.addAttribute("vehicles", reportService.getAllVehicles());
        model.addAttribute("maintenanceTypes", reportService.getAllMaintenanceTypes());
        model.addAttribute("workshops", reportService.getAllWorkshops());

        // Estadísticas generales para la página
        model.addAttribute("totalVehicles", reportService.getTotalVehicles());
        model.addAttribute("activeVehicles", reportService.getActiveVehicles());
        model.addAttribute("totalMaintenances", reportService.getTotalMaintenances());
        model.addAttribute("completedMaintenances", reportService.getCompletedMaintenances());

        return "reports";
    }

    // ========== REPORTE DE VEHÍCULOS Y ASIGNACIONES ==========

    @PostMapping("/vehicles-assignments/download")
    public ResponseEntity<InputStreamResource> downloadVehiclesAssignmentsReport(
            @RequestParam(required = false) String status) {

        ByteArrayInputStream bis = reportService.generateVehiclesAssignmentsPDF(status);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte_vehiculos_asignaciones.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // ========== REPORTE DE MANTENIMIENTOS ==========

    @PostMapping("/maintenances/download")
    public ResponseEntity<InputStreamResource> downloadMaintenancesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer vehicleId) {

        ByteArrayInputStream bis = reportService.generateMaintenancesPDF(startDate, endDate, status, vehicleId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte_mantenimientos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // ========== REPORTE DE COSTOS ==========

    @PostMapping("/costs/download")
    public ResponseEntity<InputStreamResource> downloadCostsReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer workshopId) {

        ByteArrayInputStream bis = reportService.generateCostsPDF(startDate, endDate, workshopId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte_costos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // ========== REPORTE DE ESTADÍSTICAS GENERALES ==========

    @PostMapping("/statistics/download")
    public ResponseEntity<InputStreamResource> downloadStatisticsReport() {

        ByteArrayInputStream bis = reportService.generateStatisticsPDF();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte_estadisticas.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}