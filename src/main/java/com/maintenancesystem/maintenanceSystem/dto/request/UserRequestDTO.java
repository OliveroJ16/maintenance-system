package com.maintenancesystem.maintenanceSystem.dto.request;

import com.maintenancesystem.maintenanceSystem.enums.Role;

public record UserRequestDTO(
        String username,
        String password,
        String firstName,
        String lastName,
        Role role,
        String email,
        Integer driverId
) {}