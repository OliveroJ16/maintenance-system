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
    Optional<Maintenance> findLastMaintenanceByVehicleAndType(@Param("vehicleId") Integer vehicleId, @Param("typeId") Integer typeId);

    @Query("""
        SELECT m FROM Maintenance m
        WHERE (:startDate IS NULL OR m.scheduledDate >= :startDate)
          AND (:endDate IS NULL OR m.scheduledDate <= :endDate)
          AND (:status IS NULL OR m.status = :status)
          AND (:vehicleId IS NULL OR m.vehicle.idVehicle = :vehicleId)
        """)
    List<Maintenance> filterMaintenancesDirectly(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") MaintenanceStatus status,
            @Param("vehicleId") Integer vehicleId
    );

    @Query("""
    SELECT m FROM Maintenance m
    WHERE (:startDate IS NULL OR m.scheduledDate >= :startDate)
      AND (:endDate IS NULL OR m.scheduledDate <= :endDate)
      AND (:status IS NULL OR m.status = :status)
      AND (:vehicleId IS NULL OR m.vehicle.idVehicle = :vehicleId)
      AND (:workshopId IS NULL OR m.workshop.idWorkshop = :workshopId)
""")
    List<Maintenance> filterMaintenancesWithWorkshopDirectly(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") MaintenanceStatus status,
            @Param("vehicleId") Integer vehicleId,
            @Param("workshopId") Integer workshopId
    );

    long countByStatus(MaintenanceStatus status);

    List<Maintenance> findByVehicleAndMaintenanceTypeAndStatus(Vehicle vehicle, MaintenanceType maintenanceType, MaintenanceStatus status);

}
