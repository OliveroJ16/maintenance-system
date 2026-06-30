package com.maintenancesystem.maintenanceSystem.dto.response;

import java.time.LocalDate;

public record AssignmentResponseDTO(
        Integer driverId,
        Integer vehicleId,
        String driverName,
        String vehiclePlate,
        LocalDate assignmentDate
) {}