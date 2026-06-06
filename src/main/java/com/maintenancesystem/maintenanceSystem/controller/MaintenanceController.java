package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<List<Maintenance>> getAllMaintenances() {
        return ResponseEntity.ok(
                maintenanceService.getAllMaintenances()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getMaintenanceById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                maintenanceService.getMaintenanceById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Maintenance> saveMaintenance(
            @RequestBody Maintenance maintenance) {

        return ResponseEntity.ok(
                maintenanceService.saveMaintenance(maintenance)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Maintenance> updateMaintenance(
            @PathVariable Integer id,
            @RequestBody Maintenance maintenance) {

        return ResponseEntity.ok(
                maintenanceService.updateMaintenance(id, maintenance)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(
            @PathVariable Integer id) {

        maintenanceService.deleteMaintenance(id);

        return ResponseEntity.noContent().build();
    }
}