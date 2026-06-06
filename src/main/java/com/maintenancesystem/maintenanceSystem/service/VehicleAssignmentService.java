package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignment;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignmentId;
import com.maintenancesystem.maintenanceSystem.repository.VehicleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleAssignmentService {

    private final VehicleAssignmentRepository assignmentRepository;
    private final DriverService driverService;
    private final VehicleService vehicleService;

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
        assignmentRepository.deleteByCompositeId(
                id.getDriverId(),
                id.getVehicleId()
        );
    }

    @Transactional
    public void deleteAssignmentByVehicle(Integer vehicleId) {

        VehicleAssignment currentAssignment =
                assignmentRepository.findByIdVehicleId(vehicleId)
                        .stream()
                        .findFirst()
                        .orElse(null);

        if (currentAssignment != null) {
            deleteAssignment(currentAssignment.getId());
        }
    }

    @Transactional
    public VehicleAssignment assignVehicle(
            Integer vehicleId,
            Integer driverId,
            LocalDate assignmentDate) {

        deleteAssignmentByVehicle(vehicleId);

        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        Driver driver =
                driverService.getDriverById(driverId);

        VehicleAssignmentId id =
                new VehicleAssignmentId(driverId, vehicleId);

        VehicleAssignment assignment =
                new VehicleAssignment();

        assignment.setId(id);
        assignment.setVehicle(vehicle);
        assignment.setDriver(driver);
        assignment.setAssignmentDate(assignmentDate);

        return assignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public List<VehicleAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
}