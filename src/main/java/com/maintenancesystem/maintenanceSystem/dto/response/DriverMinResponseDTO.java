package com.maintenancesystem.maintenanceSystem.dto.response;

public record DriverMinResponseDTO(
        Integer idDriver,
        String firstName,
        String lastName,
        String idCard
) {}