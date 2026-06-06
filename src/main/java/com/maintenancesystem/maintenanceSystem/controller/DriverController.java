package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDriver());
    }

    @PostMapping
    public ResponseEntity<Driver> saveDriver(@RequestBody Driver driver) {

        Driver savedDriver = driverService.saveDriver(driver);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedDriver);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(
            @PathVariable Integer id,
            @RequestBody Driver driver) {

        Driver updatedDriver = driverService.updateDriver(id, driver);

        return ResponseEntity.ok(updatedDriver);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Integer id) {

        driverService.deleteDriver(id);

        return ResponseEntity.noContent().build();
    }
}