package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceConfiguration;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maintenance-configurations")
public class MaintenanceConfigurationController {

    private final MaintenanceConfigurationService configService;

    @GetMapping
    public ResponseEntity<List<MaintenanceConfiguration>> getAllConfigurations() {
        return ResponseEntity.ok(
                configService.getAllConfigurations()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceConfiguration> getConfigurationById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                configService.getConfigurationById(id)
        );
    }

    @PostMapping
    public ResponseEntity<MaintenanceConfiguration> saveConfiguration(
            @RequestBody MaintenanceConfiguration config) {

        return ResponseEntity.ok(
                configService.saveConfiguration(config)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceConfiguration> updateConfiguration(
            @PathVariable Integer id,
            @RequestBody MaintenanceConfiguration config) {

        return ResponseEntity.ok(
                configService.updateConfiguration(config, id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable Integer id) {
        boolean deleted = configService.deleteConfiguration(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}