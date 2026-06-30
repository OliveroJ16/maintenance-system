package com.maintenancesystem.maintenanceSystem.mapper;

import com.maintenancesystem.maintenanceSystem.dto.request.DriverRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.DriverResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverMapper {

    private final StringNormalizer stringNormalizer;

    // Convertir DriverRequestDTO a Driver entity (para crear)
    public Driver toEntity(DriverRequestDTO request) {
        if (request == null) return null;

        Driver driver = new Driver();
        driver.setFirstName(stringNormalizer.toTitleCase(request.firstName()));
        driver.setLastName(stringNormalizer.toTitleCase(request.lastName()));
        driver.setIdCard(request.idCard());
        driver.setPhone(request.phone());
        driver.setEmail(request.email());
        driver.setLicenseCategory(request.licenseCategory());
        driver.setLicenseExpirationDate(request.licenseExpirationDate());
        driver.setStatus(request.status());
        return driver;
    }

    public Driver toEntity(DriverResponseDTO response) {
        if (response == null) return null;

        Driver driver = new Driver();
        driver.setIdDriver(response.idDriver());
        driver.setFirstName(response.firstName());
        driver.setLastName(response.lastName());
        driver.setIdCard(response.idCard());
        driver.setPhone(response.phone());
        driver.setEmail(response.email());
        driver.setLicenseCategory(response.licenseCategory());
        driver.setLicenseExpirationDate(response.licenseExpirationDate());
        driver.setStatus(response.status());
        return driver;
    }

    public DriverResponseDTO toResponseDTO(Driver driver) {
        if (driver == null) return null;

        return new DriverResponseDTO(
                driver.getIdDriver(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getIdCard(),
                driver.getPhone(),
                driver.getEmail(),
                driver.getLicenseCategory(),
                driver.getLicenseExpirationDate(),
                driver.getStatus()
        );
    }

    public void updateEntityFromRequest(DriverRequestDTO request, Driver driver) {
        if (request == null || driver == null) return;

        driver.setFirstName(stringNormalizer.toTitleCase(request.firstName()));
        driver.setLastName(stringNormalizer.toTitleCase(request.lastName()));
        driver.setIdCard(request.idCard());
        driver.setPhone(request.phone());
        driver.setEmail(request.email());
        driver.setLicenseCategory(request.licenseCategory());
        driver.setLicenseExpirationDate(request.licenseExpirationDate());
        driver.setStatus(request.status());
    }
}