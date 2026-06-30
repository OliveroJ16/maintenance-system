package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.dto.request.AssignmentRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.AssignmentResponseDTO;
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
    public ResponseEntity<List<AssignmentResponseDTO>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByVehicle(@PathVariable Integer vehicleId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByVehicle(vehicleId));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByDriver(@PathVariable Integer driverId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByDriver(driverId));
    }

    @GetMapping("/driver-names")
    public ResponseEntity<Map<Integer, String>> getDriverNamesByVehicle() {
        Map<Integer, String> driverMap = assignmentService.getDriverNamesByVehicleMap();
        return ResponseEntity.ok(driverMap);
    }

    @PostMapping
    public ResponseEntity<AssignmentResponseDTO> assignVehicle(@RequestBody AssignmentRequestDTO request) {
        AssignmentResponseDTO assignment = assignmentService.assignVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
    }

    @DeleteMapping("/{vehicleId}/{driverId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer vehicleId, @PathVariable Integer driverId) {
        assignmentService.deleteAssignment(vehicleId, driverId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vehicle/{vehicleId}")
    public ResponseEntity<Void> deleteAssignmentByVehicle(@PathVariable Integer vehicleId) {
        assignmentService.deleteAssignmentByVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }
}