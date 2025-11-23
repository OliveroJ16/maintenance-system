package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.enums.FuelType;
import com.maintenancesystem.maintenanceSystem.enums.VehicleStatus;
import com.maintenancesystem.maintenanceSystem.enums.VehicleType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE Vehicle v SET
            v.plate = COALESCE(:plate, v.plate),
            v.serialNumber = COALESCE(:serialNumber, v.serialNumber),
            v.mileage = COALESCE(:mileage, v.mileage),
            v.acquisitionDate = COALESCE(:acquisitionDate, v.acquisitionDate),
            v.status = COALESCE(:status, v.status),
            v.fuelType = COALESCE(:fuelType, v.fuelType),
            v.brand = COALESCE(:brand, v.brand),
            v.model = COALESCE(:model, v.model),
            v.vehicleType = COALESCE(:vehicleType, v.vehicleType)
        WHERE v.idVehicle = :idVehicle
        """)
    void updatePartial(
            @Param("idVehicle") Integer idVehicle,
            @Param("plate") String plate,
            @Param("serialNumber") String serialNumber,
            @Param("mileage") Integer mileage,
            @Param("acquisitionDate") LocalDate acquisitionDate,
            @Param("status") VehicleStatus status,
            @Param("fuelType") FuelType fuelType,
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("vehicleType") VehicleType vehicleType
    );
}
