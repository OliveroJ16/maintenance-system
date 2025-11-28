package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceConfiguration;
import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceConfigurationRepository extends JpaRepository<MaintenanceConfiguration, Integer> {
    @Query("""
        SELECT mc FROM MaintenanceConfiguration mc
        LEFT JOIN FETCH mc.vehicle
        LEFT JOIN FETCH mc.maintenanceType
        WHERE mc.vehicle.idVehicle = :vehicleId
    """)
    List<MaintenanceConfiguration> findByVehicleId(@Param("vehicleId") Integer vehicleId);

    Optional<MaintenanceConfiguration> findByVehicleAndMaintenanceType(
            Vehicle vehicle,
            MaintenanceType maintenanceType
    );
}
