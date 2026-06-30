package com.maintenancesystem.maintenanceSystem.dto.response;

import com.maintenancesystem.maintenanceSystem.enums.Role;
import java.time.LocalDateTime;

public record UserResponseDTO(
        Integer idUser,
        String username,
        String firstName,
        String lastName,
        Role role,
        String email,
        LocalDateTime registrationDate,
        DriverMinResponseDTO driver
) {}