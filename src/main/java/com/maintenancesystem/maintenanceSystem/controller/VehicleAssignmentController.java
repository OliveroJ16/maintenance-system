package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.dto.AssignmentRequest;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignment;
import com.maintenancesystem.maintenanceSystem.service.VehicleAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicle-assignments")
public class VehicleAssignmentController {

    private final VehicleAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<VehicleAssignment> assignVehicle(@RequestBody AssignmentRequest request) {
        VehicleAssignment assignment = assignmentService.assignVehicle(request.vehicleId(), request.driverId(), request.assignmentDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer vehicleId) {
        assignmentService.deleteAssignmentByVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }
}