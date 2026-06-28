package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.dto.request.AssignmentRequestDTO;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignment;
import com.maintenancesystem.maintenanceSystem.service.VehicleAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicle-assignments")
public class VehicleAssignmentController {

    private final VehicleAssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<List<VehicleAssignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/driver-names")
    public ResponseEntity<Map<Integer, String>> getDriverNamesByVehicle() {
        Map<Integer, String> driverMap = assignmentService.getDriverNamesByVehicleMap();
        return ResponseEntity.ok(driverMap);
    }

    @PostMapping
    public ResponseEntity<VehicleAssignment> assignVehicle(@RequestBody AssignmentRequestDTO request) {
        VehicleAssignment assignment = assignmentService.assignVehicle(
                request.vehicleId(),
                request.driverId(),
                request.assignmentDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer vehicleId) {
        assignmentService.deleteAssignmentByVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }
}