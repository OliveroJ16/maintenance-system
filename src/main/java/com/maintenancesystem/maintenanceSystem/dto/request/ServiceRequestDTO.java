package com.maintenancesystem.maintenanceSystem.dto.request;

import com.maintenancesystem.maintenanceSystem.enums.Status;
import java.math.BigDecimal;

public record ServiceRequestDTO(
        String serviceName,
        String description,
        BigDecimal cost,
        Integer durationMinutes,
        Status status
) {}