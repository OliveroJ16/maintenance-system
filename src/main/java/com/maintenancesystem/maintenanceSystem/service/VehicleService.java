package com.maintenancesystem.maintenanceSystem.service;


import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public void saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public boolean deleteVehicle(Integer id) {
        if (!vehicleRepository.existsById(id)) {
            return false;
        }
        vehicleRepository.deleteById(id);
        return true;
    }

    public boolean updateVehicle(Integer id, Vehicle dto) {
        int rows = vehicleRepository.updatePartial(
                id,
                dto.getPlate(),
                dto.getSerialNumber(),
                dto.getMileage(),
                dto.getAcquisitionDate(),
                dto.getStatus(),
                dto.getFuelType(),
                dto.getBrand(),
                dto.getModel(),
                dto.getVehicleType()
        );

        return rows > 0;
    }
}

