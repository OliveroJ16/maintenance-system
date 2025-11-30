package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.enums.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {

    @Query("""
    SELECT m
    FROM Maintenance m
    WHERE m.vehicle.idVehicle = :vehicleId
      AND m.maintenanceType.idMaintenanceType = :typeId
      AND m.status = 'COMPLETADO'
    ORDER BY m.executionDate DESC, m.idMaintenance DESC
""")
    Optional<Maintenance> findLastMaintenanceByVehicleAndType(
            @Param("vehicleId") Integer vehicleId,
            @Param("typeId") Integer typeId
    );

    List<Maintenance> findByVehicleIdVehicle(Integer vehicleId);
    List<Maintenance> findByScheduledDateBetween(LocalDate startDate, LocalDate endDate);
    List<Maintenance> findByStatus(String status);

    /**
     * Busca mantenimientos por vehículo, tipo y estado
     */
    List<Maintenance> findByVehicleAndMaintenanceTypeAndStatus(
            Vehicle vehicle,
            MaintenanceType maintenanceType,
            MaintenanceStatus status
    );

    /**
     * Busca mantenimientos por vehículo y estado
     */
    List<Maintenance> findByVehicleAndStatus(Vehicle vehicle, MaintenanceStatus status);

    /**
     * Busca mantenimientos por estado
     */
    List<Maintenance> findByStatus(MaintenanceStatus status);
}
