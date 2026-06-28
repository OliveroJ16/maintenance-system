package com.maintenancesystem.maintenanceSystem.dto.response;

import com.maintenancesystem.maintenanceSystem.enums.Status;

public record WorkshopResponseDTO(
        Integer idWorkshop,
        String workshopName,
        String address,
        String phone,
        String email,
        String specialty,
        Status status
) {}