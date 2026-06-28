package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.dto.request.WorkshopRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.WorkshopResponseDTO;
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

    @GetMapping
    public ResponseEntity<List<WorkshopResponseDTO>> getAllWorkshops() {
        return ResponseEntity.ok(workshopService.getAllWorkshop());
    }

    @PostMapping
    public ResponseEntity<WorkshopResponseDTO> saveWorkshop(@RequestBody WorkshopRequestDTO workshopDTO) {
        WorkshopResponseDTO savedWorkshop = workshopService.saveWorkshop(workshopDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWorkshop);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkshopResponseDTO> updateWorkshop(@PathVariable Integer id, @RequestBody WorkshopRequestDTO workshopDTO) {
        WorkshopResponseDTO updatedWorkshop = workshopService.updateWorkshop(id, workshopDTO);
        return ResponseEntity.ok(updatedWorkshop);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkshop(@PathVariable Integer id) {
        workshopService.deleteWorkshop(id);
        return ResponseEntity.noContent().build();
    }

}