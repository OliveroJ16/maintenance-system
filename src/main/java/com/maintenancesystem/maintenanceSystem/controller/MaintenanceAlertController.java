package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceAlert;
import com.maintenancesystem.maintenanceSystem.enums.AlertStatus;
import com.maintenancesystem.maintenanceSystem.enums.AlertType;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class MaintenanceAlertController {

    private final MaintenanceAlertService alertService;

    @GetMapping
    public ResponseEntity<List<MaintenanceAlert>> getAlerts(
            @RequestParam(required = false) AlertType type,
            @RequestParam(required = false) Boolean viewed,
            @RequestParam(required = false) Boolean urgent) {

        List<MaintenanceAlert> alerts;

        if (Boolean.TRUE.equals(urgent)) {
            alerts = alertService.getUrgentAlerts();
        }
        else if (viewed != null && !viewed) {
            alerts = alertService.getUnviewedAlerts();
        }
        else if (type != null) {
            alerts = alertService.getAlertsByType(type);
        }
        else {
            alerts = alertService.getAllAlerts();
        }

        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getSummary() {
        return ResponseEntity.ok(
                alertService.getAlertsSummary()
        );
    }

    @PatchMapping("/{id}/viewed")
    public ResponseEntity<Void> markAsViewed(
            @PathVariable Integer id) {

        alertService.markAsViewed(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Integer id,
            @RequestParam AlertStatus status) {

        alertService.updateAlertStatus(id, status);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/viewed")
    public ResponseEntity<Void> markAllAsViewed() {

        alertService.markAllAsViewed();

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate-preventive")
    public ResponseEntity<Void> generatePreventiveAlerts() {

        alertService.generatePreventiveAlerts();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-expired")
    public ResponseEntity<Void> updateExpiredAlerts() {

        alertService.updateExpiredAlerts();

        return ResponseEntity.ok().build();
    }
}