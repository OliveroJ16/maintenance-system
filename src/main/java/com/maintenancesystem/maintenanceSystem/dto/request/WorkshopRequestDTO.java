package com.maintenancesystem.maintenanceSystem.dto.request;

import com.maintenancesystem.maintenanceSystem.enums.Status;

public record WorkshopRequestDTO(
        String workshopName,
        String address,
        String phone,
        String email,
        String specialty,
        Status status
) {}