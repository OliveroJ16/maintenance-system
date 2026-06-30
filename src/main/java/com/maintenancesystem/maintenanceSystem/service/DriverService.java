package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.dto.request.DriverRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.DriverResponseDTO;
import java.util.List;

public interface DriverService {
    List<DriverResponseDTO> getAllDriver();
    DriverResponseDTO getDriverById(Integer id);
    DriverResponseDTO saveDriver(DriverRequestDTO driverRequestDTO);
    DriverResponseDTO updateDriver(Integer id, DriverRequestDTO driverRequestDTO);
    void deleteDriver(Integer id);
}