package com.maintenancesystem.maintenanceSystem.dto.request;

import com.maintenancesystem.maintenanceSystem.enums.LicenseCategory;
import com.maintenancesystem.maintenanceSystem.enums.Status;
import java.time.LocalDate;

public record DriverRequestDTO(
        String firstName,
        String lastName,
        String idCard,
        String phone,
        String email,
        LicenseCategory licenseCategory,
        LocalDate licenseExpirationDate,
        Status status
) {}