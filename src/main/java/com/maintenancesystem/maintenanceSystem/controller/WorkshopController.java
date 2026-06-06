package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Service;
import com.maintenancesystem.maintenanceSystem.entity.Workshop;
import com.maintenancesystem.maintenanceSystem.service.ServiceService;
import com.maintenancesystem.maintenanceSystem.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;
    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<Workshop>> getAllWorkshops() {
        return ResponseEntity.ok(workshopService.getAllWorkshop());
    }

    @PostMapping
    public ResponseEntity<Workshop> saveWorkshop(@RequestBody Workshop workshop) {
        Workshop savedWorkshop = workshopService.saveWorkshop(workshop);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWorkshop);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workshop> updateWorkshop(@PathVariable Integer id, @RequestBody Workshop workshop) {
        Workshop updatedWorkshop = workshopService.updateWorkshop(id, workshop);
        return ResponseEntity.ok(updatedWorkshop);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkshop(@PathVariable Integer id) {
        workshopService.deleteWorkshop(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<List<Service>> getWorkshopServices(@PathVariable Integer id) {
        return ResponseEntity.ok(serviceService.getAllService(id));
    }
}