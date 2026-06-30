package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.dto.request.AssignmentRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.AssignmentResponseDTO;
import java.util.List;
import java.util.Map;

public interface VehicleAssignmentService {
    List<AssignmentResponseDTO> getAllAssignments();
    List<AssignmentResponseDTO> getAssignmentsByVehicle(Integer vehicleId);
    List<AssignmentResponseDTO> getAssignmentsByDriver(Integer driverId);
    AssignmentResponseDTO assignVehicle(AssignmentRequestDTO request);
    void deleteAssignment(Integer vehicleId, Integer driverId);
    void deleteAssignmentByVehicle(Integer vehicleId);
    Map<Integer, String> getDriverNamesByVehicleMap();
}