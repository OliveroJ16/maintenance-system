package com.maintenancesystem.maintenanceSystem.dto.response;

import com.maintenancesystem.maintenanceSystem.enums.Status;
import java.math.BigDecimal;

public record ServiceResponseDTO(
        Integer idService,
        String serviceName,
        String description,
        BigDecimal cost,
        Integer durationMinutes,
        Status status,
        WorkshopMinResponseDTO workshop
) {}