package com.maintenancesystem.maintenanceSystem.service.impl;

import com.maintenancesystem.maintenanceSystem.dto.request.AssignmentRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.AssignmentResponseDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.DriverResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignment;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignmentId;
import com.maintenancesystem.maintenanceSystem.mapper.DriverMapper;
import com.maintenancesystem.maintenanceSystem.mapper.VehicleAssignmentMapper;
import com.maintenancesystem.maintenanceSystem.repository.VehicleAssignmentRepository;
import com.maintenancesystem.maintenanceSystem.repository.VehicleRepository;
import com.maintenancesystem.maintenanceSystem.service.VehicleAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleAssignmentServiceImpl implements VehicleAssignmentService {

    private final VehicleAssignmentRepository assignmentRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverServiceImpl driverService;
    private final DriverMapper driverMapper;
    private final VehicleAssignmentMapper assignmentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> getAllAssignments() {
        return assignmentRepository.findAll()
                .stream()
                .map(assignmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> getAssignmentsByVehicle(Integer vehicleId) {
        return assignmentRepository.findByIdVehicleId(vehicleId)
                .stream()
                .map(assignmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> getAssignmentsByDriver(Integer driverId) {
        return assignmentRepository.findByIdDriverId(driverId)
                .stream()
                .map(assignmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssignmentResponseDTO assignVehicle(AssignmentRequestDTO request) {
        try {
            Integer vehicleId = request.vehicleId();
            Integer driverId = request.driverId();
            LocalDate assignmentDate = request.assignmentDate();

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

            return assignmentMapper.toResponseDTO(saved);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteAssignment(Integer vehicleId, Integer driverId) {
        VehicleAssignmentId id = new VehicleAssignmentId(driverId, vehicleId);
        assignmentRepository.deleteByCompositeId(driverId, vehicleId);
    }

    @Override
    @Transactional
    public void deleteAssignmentByVehicle(Integer vehicleId) {
        VehicleAssignment currentAssignment = assignmentRepository.findByIdVehicleId(vehicleId)
                .stream()
                .findFirst()
                .orElse(null);

        if (currentAssignment != null) {
            assignmentRepository.deleteByCompositeId(
                    currentAssignment.getId().getDriverId(),
                    currentAssignment.getId().getVehicleId()
            );
        }
    }

    @Override
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