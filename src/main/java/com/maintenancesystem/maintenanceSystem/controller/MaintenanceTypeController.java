package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maintenance-types")
public class MaintenanceTypeController {

    private final MaintenanceTypeService maintenanceTypeService;

    @GetMapping
    public ResponseEntity<List<MaintenanceType>> getAllMaintenanceTypes() {
        return ResponseEntity.ok(
                maintenanceTypeService.getAllMaintenanceType()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceType> getMaintenanceTypeById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                maintenanceTypeService.getMaintenanceTypeById(id)
        );
    }

    @PostMapping
    public ResponseEntity<MaintenanceType> saveMaintenanceType(
            @RequestBody MaintenanceType maintenanceType) {

        MaintenanceType saved =
                maintenanceTypeService.saveMaintenanceType(maintenanceType);

        return ResponseEntity
                .created(URI.create("/api/maintenance-types/" + saved.getIdMaintenanceType()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceType> updateMaintenanceType(
            @PathVariable Integer id,
            @RequestBody MaintenanceType maintenanceType) {

        MaintenanceType updated =
                maintenanceTypeService.updateMaintenanceType(maintenanceType, id);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceType(
            @PathVariable Integer id) {

        boolean deleted =
                maintenanceTypeService.deleteMaintenanceType(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}