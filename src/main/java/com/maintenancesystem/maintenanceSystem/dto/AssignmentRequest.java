package com.maintenancesystem.maintenanceSystem.dto;

import java.time.LocalDate;

public record AssignmentRequest(
        Integer vehicleId,
        Integer driverId,
        LocalDate assignmentDate
) {
}