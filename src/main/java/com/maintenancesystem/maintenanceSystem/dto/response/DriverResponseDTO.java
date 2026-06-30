package com.maintenancesystem.maintenanceSystem.dto.response;

import com.maintenancesystem.maintenanceSystem.enums.LicenseCategory;
import com.maintenancesystem.maintenanceSystem.enums.Status;
import java.time.LocalDate;

public record DriverResponseDTO(
        Integer idDriver,
        String firstName,
        String lastName,
        String idCard,
        String phone,
        String email,
        LicenseCategory licenseCategory,
        LocalDate licenseExpirationDate,
        Status status
) {}