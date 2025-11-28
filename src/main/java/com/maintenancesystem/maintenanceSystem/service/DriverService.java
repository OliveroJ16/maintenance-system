package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.repository.DriverRepository;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final StringNormalizer stringNormalizer;

    public List<Driver> getAllDriver(){
        return driverRepository.findAll();
    }

    public void saveDriver(Driver driver){
        driver.setFirstName(stringNormalizer.toTitleCase(driver.getFirstName()));
        driver.setLastName(stringNormalizer.toTitleCase(driver.getLastName()));
        driverRepository.save(driver);
    }

    public void deleteDriver(Integer id) {
        driverRepository.deleteById(id);
    }

    public void updateDriver(Integer id, Driver driver) {
        driver.setFirstName(stringNormalizer.toTitleCase(driver.getFirstName()));
        driver.setLastName(stringNormalizer.toTitleCase(driver.getLastName()));
        driverRepository.updatePartial(
                id,
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

    public Driver getDriverById(Integer id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado con id: " + id));
    }

}
