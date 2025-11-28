package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
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

}
