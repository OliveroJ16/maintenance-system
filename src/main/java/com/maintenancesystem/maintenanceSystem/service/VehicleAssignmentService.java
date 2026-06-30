package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.dto.response.DriverResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignment;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignmentId;
import com.maintenancesystem.maintenanceSystem.mapper.DriverMapper;
import com.maintenancesystem.maintenanceSystem.repository.VehicleAssignmentRepository;
import com.maintenancesystem.maintenanceSystem.repository.VehicleRepository;
import com.maintenancesystem.maintenanceSystem.service.impl.DriverServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleAssignmentService {

    private final VehicleAssignmentRepository assignmentRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverServiceImpl driverService;
    private final DriverMapper driverMapper;

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
    public VehicleAssignment assignVehicle(Integer vehicleId, Integer driverId, LocalDate assignmentDate) {
        try {
            List<VehicleAssignment> existing = assignmentRepository.findByIdVehicleId(vehicleId);

            if (!existing.isEmpty()) {
                assignmentRepository.deleteAll(existing);
                assignmentRepository.flush();
            }

            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + vehicleId));

            DriverResponseDTO driverResponseDTO = driverService.getDriverById(driverId);
            Driver driver = driverMapper.toEntity(driverResponseDTO);

            VehicleAssignmentId id = new VehicleAssignmentId(driverId, vehicleId);

            VehicleAssignment assignment = new VehicleAssignment();
            assignment.setId(id);
            assignment.setVehicle(vehicle);
            assignment.setDriver(driver);
            assignment.setAssignmentDate(assignmentDate);

            VehicleAssignment saved = assignmentRepository.save(assignment);
            assignmentRepository.flush();
            return saved;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<VehicleAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Map<Integer, String> getDriverNamesByVehicleMap() {
        List<VehicleAssignment> assignments = assignmentRepository.findAll();

        return assignments.stream()
                .collect(Collectors.toMap(
                        assignment -> assignment.getVehicle().getIdVehicle(),
                        assignment -> assignment.getDriver().getFirstName() + " " + assignment.getDriver().getLastName(),
                        (existing, replacement) -> replacement
                ));
    }
}