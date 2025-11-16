package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public List<Driver> getAllDriver(){
        List<Driver> drivers = driverRepository.findAll();
        return drivers;
    }

    public void saveDriver(Driver driver){
        driverRepository.save(driver);
    }

    public boolean deleteDriver(Integer id) {
        if (!driverRepository.existsById(id)) {
            return false;
        }

        driverRepository.deleteById(id);
        return true;
    }

    public boolean updateDriver(Integer id, Driver dto) {
        int rows = driverRepository.updatePartial(
                id,
                dto.getFirstName(),
                dto.getLastName(),
                dto.getIdCard(),
                dto.getPhone(),
                dto.getEmail(),
                dto.getLicenseCategory(),
                dto.getLicenseExpirationDate(),
                dto.getStatus()
        );

        return rows > 0;
    }

}
