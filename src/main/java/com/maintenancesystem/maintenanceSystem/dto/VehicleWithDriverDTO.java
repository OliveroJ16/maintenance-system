package com.maintenancesystem.maintenanceSystem.dto;

import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleWithDriverDTO {
    private Vehicle vehicle;
    private String driverName;
}
