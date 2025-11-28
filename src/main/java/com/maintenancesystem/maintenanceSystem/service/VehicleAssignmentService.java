package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignment;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignmentId;
import com.maintenancesystem.maintenanceSystem.repository.VehicleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleAssignmentService {

    private final VehicleAssignmentRepository assignmentRepository;

    @Transactional
    public VehicleAssignment saveAssignment(VehicleAssignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public List<VehicleAssignment> getAssignmentsByVehicle(Integer vehicleId) {
        return assignmentRepository.findByIdVehicleId(vehicleId);
    }

    @Transactional(readOnly = true)
    public List<VehicleAssignment> getAssignmentsByDriver(Integer driverId) {
        return assignmentRepository.findByIdDriverId(driverId);
    }

    @Transactional
    public void deleteAssignment(VehicleAssignmentId id) {
        assignmentRepository.deleteByCompositeId(id.getDriverId(), id.getVehicleId());
    }

    @Transactional(readOnly = true)
    public List<VehicleAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
}