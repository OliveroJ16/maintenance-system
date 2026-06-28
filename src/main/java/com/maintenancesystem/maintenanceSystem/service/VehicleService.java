package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.dto.request.VehicleRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.VehicleResponseDTO;

import java.util.List;

public interface VehicleService {
    List<VehicleResponseDTO> getAllVehicles();
    VehicleResponseDTO saveVehicle(VehicleRequestDTO request);
    VehicleResponseDTO updateVehicle(Integer id, VehicleRequestDTO request);
    VehicleResponseDTO getVehicleById(Integer id);
    void deleteVehicle(Integer id);
    long countActiveVehicles();
}