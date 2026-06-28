package com.maintenancesystem.maintenanceSystem.mapper;

import com.maintenancesystem.maintenanceSystem.dto.request.VehicleRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.VehicleResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleMapper {

    private final StringNormalizer stringNormalizer;

    public Vehicle toEntity(VehicleRequestDTO request) {
        if (request == null) return null;

        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(request.plate() != null ? request.plate().toUpperCase().trim() : null);
        vehicle.setSerialNumber(request.serialNumber() != null ? request.serialNumber().toUpperCase().trim() : null);
        vehicle.setMileage(request.mileage());
        vehicle.setAcquisitionDate(request.acquisitionDate());
        vehicle.setStatus(request.status());
        vehicle.setFuelType(request.fuelType());
        vehicle.setBrand(stringNormalizer.toTitleCase(request.brand()));
        vehicle.setModel(stringNormalizer.toTitleCase(request.model()));
        vehicle.setVehicleType(request.vehicleType());
        return vehicle;
    }

    public VehicleResponseDTO toResponseDTO(Vehicle vehicle) {
        if (vehicle == null) return null;

        return new VehicleResponseDTO(
                vehicle.getIdVehicle(),
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
    }
}