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

    public Vehicle saveVehicle(Vehicle vehicle) {
        vehicle.setModel(stringNormalizer.toTitleCase(vehicle.getModel()));
        vehicle.setBrand(stringNormalizer.toTitleCase(vehicle.getBrand()));
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Integer id) {
        vehicleRepository.deleteById(id);
    }

    public Vehicle updateVehicle(Integer id, Vehicle vehicle) {
        vehicle.setModel(stringNormalizer.toTitleCase(vehicle.getModel()));
        vehicle.setBrand(stringNormalizer.toTitleCase(vehicle.getBrand()));
        vehicleRepository.updatePartial(
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
        return vehicle;
    }

    public Vehicle getVehicleById(Integer id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
    }
}

