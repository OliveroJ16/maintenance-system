package com.maintenancesystem.maintenanceSystem.service;


import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.repository.VehicleRepository;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final StringNormalizer stringNormalizer;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public void saveVehicle(Vehicle vehicle) {
        vehicle.setModel(stringNormalizer.toTitleCase(vehicle.getModel()));
        vehicle.setBrand(stringNormalizer.toTitleCase(vehicle.getBrand()));
        vehicleRepository.save(vehicle);
    }

    public boolean deleteVehicle(Integer id) {
        if (!vehicleRepository.existsById(id)) {
            return false;
        }
        vehicleRepository.deleteById(id);
        return true;
    }

    public boolean updateVehicle(Integer id, Vehicle vehicle) {
        vehicle.setModel(stringNormalizer.toTitleCase(vehicle.getModel()));
        vehicle.setBrand(stringNormalizer.toTitleCase(vehicle.getBrand()));
        int rows = vehicleRepository.updatePartial(
                id,
                vehicle.getPlate(),
                vehicle.getSerialNumber(),
                vehicle.getMileage(),
                vehicle.getAcquisitionDate(),
                vehicle.getStatus(),
                vehicle.getFuelType(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getVehicleType()
        );

        return rows > 0;
    }
}

