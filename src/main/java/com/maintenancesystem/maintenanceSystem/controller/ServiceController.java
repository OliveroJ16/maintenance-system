package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Service;
import com.maintenancesystem.maintenanceSystem.entity.Workshop;
import com.maintenancesystem.maintenanceSystem.service.ServiceService;
import com.maintenancesystem.maintenanceSystem.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;
    private final WorkshopService workshopService;

    @PostMapping
    public ResponseEntity<Service> saveService(@RequestBody Service service, @RequestParam Integer workshopId) {
        Workshop workshop = workshopService.getWorkshopById(workshopId);
        service.setWorkshop(workshop);
        Service savedService = serviceService.saveService(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Integer id, @RequestBody Service service) {
        Service updatedService = serviceService.updateService(service, id);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}