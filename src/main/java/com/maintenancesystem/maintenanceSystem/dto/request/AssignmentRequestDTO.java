package com.maintenancesystem.maintenanceSystem.dto.request;

import java.time.LocalDate;

public record AssignmentRequestDTO(
        Integer vehicleId,
        Integer driverId,
        LocalDate assignmentDate
) {
}