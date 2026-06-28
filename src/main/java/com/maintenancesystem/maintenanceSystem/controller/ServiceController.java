package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.dto.request.ServiceRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.ServiceResponseDTO;
import com.maintenancesystem.maintenanceSystem.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<ServiceResponseDTO> saveService(@RequestBody ServiceRequestDTO serviceDTO, @RequestParam Integer workshopId) {
        ServiceResponseDTO savedService = serviceService.saveService(serviceDTO, workshopId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponseDTO> updateService(@PathVariable Integer id, @RequestBody ServiceRequestDTO serviceDTO) {
        ServiceResponseDTO updatedService = serviceService.updateService(id, serviceDTO);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/workshops/{id}")
    public ResponseEntity<List<ServiceResponseDTO>> getWorkshopServices(@PathVariable Integer id) {
        return ResponseEntity.ok(serviceService.getAllService(id));
    }
}