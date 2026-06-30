package com.maintenancesystem.maintenanceSystem.mapper;

import com.maintenancesystem.maintenanceSystem.dto.response.AssignmentResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignment;
import org.springframework.stereotype.Component;

@Component
public class VehicleAssignmentMapper {

    public AssignmentResponseDTO toResponseDTO(VehicleAssignment assignment) {
        if (assignment == null) return null;

        return new AssignmentResponseDTO(
                assignment.getDriver().getIdDriver(),
                assignment.getVehicle().getIdVehicle(),
                assignment.getDriver().getFirstName() + " " + assignment.getDriver().getLastName(),
                assignment.getVehicle().getPlate(),
                assignment.getAssignmentDate()
        );
    }
}