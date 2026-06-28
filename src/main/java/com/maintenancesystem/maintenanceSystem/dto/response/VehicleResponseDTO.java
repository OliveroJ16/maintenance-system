package com.maintenancesystem.maintenanceSystem.dto.response;

import com.maintenancesystem.maintenanceSystem.enums.*;
import java.time.LocalDate;

public record VehicleResponseDTO(
        Integer idVehicle,
        String plate,
        String serialNumber,
        Integer mileage,
        LocalDate acquisitionDate,
        VehicleStatus status,
        FuelType fuelType,
        String brand,
        String model,
        VehicleType vehicleType
) {}