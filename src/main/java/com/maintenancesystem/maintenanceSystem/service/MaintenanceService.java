package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.entity.Workshop;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceRepository;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceTypeRepository;
import com.maintenancesystem.maintenanceSystem.repository.VehicleRepository;
import com.maintenancesystem.maintenanceSystem.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final WorkshopRepository workshopRepository;

    public List<Maintenance> getAllMaintenance() {
        return maintenanceRepository.findAll();
    }

    public Maintenance getMaintenanceById(Integer id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));
    }

    @Transactional
    public Maintenance saveMaintenance(Maintenance maintenance) {
        // Consultar el vehículo completo
        Vehicle vehicle = vehicleRepository.findById(maintenance.getVehicle().getIdVehicle())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        // Consultar el tipo de mantenimiento completo
        MaintenanceType maintenanceType = maintenanceTypeRepository
                .findById(maintenance.getMaintenanceType().getIdMaintenanceType())
                .orElseThrow(() -> new RuntimeException("Tipo de mantenimiento no encontrado"));

        maintenance.setVehicle(vehicle);
        maintenance.setMaintenanceType(maintenanceType);

        // Workshop es opcional
        if (maintenance.getWorkshop() != null && maintenance.getWorkshop().getIdWorkshop() != null) {
            Workshop workshop = workshopRepository.findById(maintenance.getWorkshop().getIdWorkshop())
                    .orElseThrow(() -> new RuntimeException("Taller no encontrado"));
            maintenance.setWorkshop(workshop);
        }

        return maintenanceRepository.save(maintenance);
    }

    @Transactional
    public Maintenance updateMaintenance(Maintenance maintenance, Integer id) {
        Maintenance existing = getMaintenanceById(id);

        // Consultar el vehículo completo
        Vehicle vehicle = vehicleRepository.findById(maintenance.getVehicle().getIdVehicle())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        // Consultar el tipo de mantenimiento completo
        MaintenanceType maintenanceType = maintenanceTypeRepository
                .findById(maintenance.getMaintenanceType().getIdMaintenanceType())
                .orElseThrow(() -> new RuntimeException("Tipo de mantenimiento no encontrado"));

        existing.setScheduledDate(maintenance.getScheduledDate());
        existing.setScheduledKm(maintenance.getScheduledKm());
        existing.setExecutionDate(maintenance.getExecutionDate());
        existing.setExecutionKm(maintenance.getExecutionKm());
        existing.setStatus(maintenance.getStatus());
        existing.setDescription(maintenance.getDescription());
        existing.setVehicle(vehicle);
        existing.setMaintenanceType(maintenanceType);

        // Workshop es opcional
        if (maintenance.getWorkshop() != null && maintenance.getWorkshop().getIdWorkshop() != null) {
            Workshop workshop = workshopRepository.findById(maintenance.getWorkshop().getIdWorkshop())
                    .orElseThrow(() -> new RuntimeException("Taller no encontrado"));
            existing.setWorkshop(workshop);
        } else {
            existing.setWorkshop(null);
        }

        return maintenanceRepository.save(existing);
    }

    @Transactional
    public void deleteMaintenance(Integer id) {
        maintenanceRepository.deleteById(id);
    }
}