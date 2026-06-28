package com.maintenancesystem.maintenanceSystem.dto.request;

import com.maintenancesystem.maintenanceSystem.enums.*;
import java.time.LocalDate;

public record VehicleRequestDTO(
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