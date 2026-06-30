package com.maintenancesystem.maintenanceSystem.mapper;

import com.maintenancesystem.maintenanceSystem.dto.request.UserRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.DriverMinResponseDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.UserResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final StringNormalizer stringNormalizer;
    private final BCryptPasswordEncoder passwordEncoder;

    public User toEntity(UserRequestDTO request) {
        if (request == null) return null;

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(stringNormalizer.toTitleCase(request.firstName()));
        user.setLastName(stringNormalizer.toTitleCase(request.lastName()));
        user.setRole(request.role());
        user.setEmail(request.email());
        user.setRegistrationDate(LocalDateTime.now());
        return user;
    }

    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) return null;

        DriverMinResponseDTO driverMin = null;
        if (user.getDriver() != null) {
            driverMin = new DriverMinResponseDTO(
                    user.getDriver().getIdDriver(),
                    user.getDriver().getFirstName(),
                    user.getDriver().getLastName(),
                    user.getDriver().getIdCard()
            );
        }

        return new UserResponseDTO(
                user.getIdUser(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getEmail(),
                user.getRegistrationDate(),
                driverMin
        );
    }

    public void updateEntityFromRequest(UserRequestDTO request, User user) {
        if (request == null || user == null) return;

        user.setFirstName(stringNormalizer.toTitleCase(request.firstName()));
        user.setLastName(stringNormalizer.toTitleCase(request.lastName()));
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setUsername(request.username());

        // Solo actualizar la contraseña si se proporciona una nueva
        if (request.password() != null && !request.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
    }
}