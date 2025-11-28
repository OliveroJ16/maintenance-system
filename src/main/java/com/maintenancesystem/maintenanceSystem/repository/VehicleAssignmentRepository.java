package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignment;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleAssignmentRepository extends JpaRepository<VehicleAssignment, VehicleAssignmentId> {

    @Query("SELECT va FROM VehicleAssignment va JOIN FETCH va.driver JOIN FETCH va.vehicle WHERE va.id.vehicleId = :vehicleId")
    List<VehicleAssignment> findByIdVehicleId(@Param("vehicleId") Integer vehicleId);

    @Query("SELECT va FROM VehicleAssignment va JOIN FETCH va.driver JOIN FETCH va.vehicle WHERE va.id.driverId = :driverId")
    List<VehicleAssignment> findByIdDriverId(@Param("driverId") Integer driverId);

    @Query("SELECT va FROM VehicleAssignment va JOIN FETCH va.driver JOIN FETCH va.vehicle")
    List<VehicleAssignment> findAll();

    @Modifying
    @Query("DELETE FROM VehicleAssignment va WHERE va.id.driverId = :driverId AND va.id.vehicleId = :vehicleId")
    void deleteByCompositeId(@Param("driverId") Integer driverId, @Param("vehicleId") Integer vehicleId);
}