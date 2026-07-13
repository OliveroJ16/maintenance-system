package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.*;
import com.maintenancesystem.maintenanceSystem.enums.MaintenanceStatus;
import com.maintenancesystem.maintenanceSystem.enums.VehicleStatus;
import com.maintenancesystem.maintenanceSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportDataService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    private final WorkshopRepository workshopRepository;
    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final VehicleAssignmentRepository vehicleAssignmentRepository;
    private final ServiceRepository serviceRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<MaintenanceType> getAllMaintenanceTypes() {
        return maintenanceTypeRepository.findAll();
    }

    public List<Workshop> getAllWorkshops() {
        return workshopRepository.findAll();
    }

    public long getTotalVehicles() {
        return vehicleRepository.count();
    }

    public long getActiveVehicles() {
        return vehicleRepository.countByStatus(VehicleStatus.ACTIVO);
    }

    public long getTotalMaintenances() {
        return maintenanceRepository.count();
    }

    public long getCompletedMaintenances() {
        return maintenanceRepository.countByStatus(MaintenanceStatus.COMPLETADO);
    }

    public List<Vehicle> filterVehiclesByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status);
    }

    public List<Maintenance> filterMaintenances(LocalDate startDate, LocalDate endDate,
                                                MaintenanceStatus status, Integer vehicleId) {
        return maintenanceRepository.filterMaintenancesDirectly(startDate, endDate, status, vehicleId);
    }

    public List<Maintenance> filterMaintenances(LocalDate startDate, LocalDate endDate,
                                                MaintenanceStatus status, Integer vehicleId, Integer workshopId) {
        return maintenanceRepository.filterMaintenancesWithWorkshopDirectly(startDate, endDate, status, vehicleId, workshopId);
    }


    public Map<String, List<Maintenance>> groupMaintenancesByWorkshop(List<Maintenance> maintenances) {
        return maintenances.stream()
                .filter(m -> m.getWorkshop() != null)
                .collect(Collectors.groupingBy(m -> m.getWorkshop().getWorkshopName()));
    }

    public Map<String, Long> getMaintenancesByType(List<Maintenance> maintenances) {
        return maintenances.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getMaintenanceType().getTypeName(),
                        Collectors.counting()
                ));
    }
    public String getDriverForVehicle(Integer vehicleId) {
        List<VehicleAssignment> assignments = vehicleAssignmentRepository.findAll().stream()
                .filter(a -> a.getVehicle().getIdVehicle().equals(vehicleId))
                .collect(Collectors.toList());

        if (assignments.isEmpty()) {
            return "Sin asignar";
        }

        VehicleAssignment latest = assignments.stream()
                .max(Comparator.comparing(VehicleAssignment::getAssignmentDate))
                .orElse(null);

        if (latest != null && latest.getDriver() != null) {
            return latest.getDriver().getFirstName() + " " + latest.getDriver().getLastName();
        }

        return "Sin asignar";
    }

    public List<com.maintenancesystem.maintenanceSystem.entity.Service> getServicesForMaintenance(Integer maintenanceId, Integer workshopId) {
        return serviceRepository.findAll().stream()
                .filter(s -> s.getWorkshop().getIdWorkshop().equals(workshopId))
                .limit(2)
                .collect(Collectors.toList());
    }
    public Map<String, Long> getVehicleStatistics(List<Vehicle> vehicles) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) vehicles.size());
        stats.put("active", vehicles.stream().filter(v -> v.getStatus().name().equals("ACTIVO")).count());
        stats.put("inactive", vehicles.stream().filter(v -> v.getStatus().name().equals("INACTIVO")).count());
        stats.put("maintenance", vehicles.stream().filter(v -> v.getStatus().name().equals("MANTENIMIENTO")).count());
        stats.put("totalMileage", (long) vehicles.stream().mapToInt(Vehicle::getMileage).sum());
        return stats;
    }

    public Map<String, Long> getMaintenanceStatistics(List<Maintenance> maintenances) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) maintenances.size());
        stats.put("completed", maintenances.stream().filter(m -> m.getStatus().name().equals("COMPLETADO")).count());
        stats.put("pending", maintenances.stream().filter(m -> m.getStatus().name().equals("PENDIENTE")).count());
        stats.put("inProcess", maintenances.stream().filter(m -> m.getStatus().name().equals("EN_PROCESO")).count());
        return stats;
    }
    public BigDecimal calculateMaintenanceCost(Maintenance maintenance) {
        List<com.maintenancesystem.maintenanceSystem.entity.Service> services = getServicesForMaintenance(
                maintenance.getIdMaintenance(),
                maintenance.getWorkshop().getIdWorkshop()
        );
        return services.stream()
                .map(com.maintenancesystem.maintenanceSystem.entity.Service::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}