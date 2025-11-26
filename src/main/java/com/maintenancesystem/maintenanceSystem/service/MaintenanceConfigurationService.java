package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceConfiguration;
import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceConfigurationRepository;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceTypeRepository;
import com.maintenancesystem.maintenanceSystem.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceConfigurationService {

    private final MaintenanceConfigurationRepository configRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceTypeRepository maintenanceTypeRepository;

    public List<MaintenanceConfiguration> getAllConfigurations() {
        return configRepository.findAll();
    }

    public MaintenanceConfiguration getConfigurationById(Integer id) {
        return configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada"));
    }

    @Transactional
    public MaintenanceConfiguration saveConfiguration(MaintenanceConfiguration config) {
        // Consultar el vehículo completo
        Vehicle vehicle = vehicleRepository.findById(config.getVehicle().getIdVehicle())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        // Consultar el tipo de mantenimiento completo
        MaintenanceType maintenanceType = maintenanceTypeRepository.findById(config.getMaintenanceType().getIdMaintenanceType())
                .orElseThrow(() -> new RuntimeException("Tipo de mantenimiento no encontrado"));

        config.setVehicle(vehicle);
        config.setMaintenanceType(maintenanceType);

        return configRepository.save(config);
    }

    @Transactional
    public MaintenanceConfiguration updateConfiguration(MaintenanceConfiguration config, Integer id) {
        MaintenanceConfiguration existing = getConfigurationById(id);

        // Consultar el vehículo completo
        Vehicle vehicle = vehicleRepository.findById(config.getVehicle().getIdVehicle())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        // Consultar el tipo de mantenimiento completo
        MaintenanceType maintenanceType = maintenanceTypeRepository.findById(config.getMaintenanceType().getIdMaintenanceType())
                .orElseThrow(() -> new RuntimeException("Tipo de mantenimiento no encontrado"));

        existing.setFrequencyKm(config.getFrequencyKm());
        existing.setFrequencyMonths(config.getFrequencyMonths());
        existing.setDescription(config.getDescription());
        existing.setMaintenanceType(maintenanceType);
        existing.setVehicle(vehicle);

        return configRepository.save(existing);
    }

    @Transactional
    public void deleteConfiguration(Integer id) {
        configRepository.deleteById(id);
    }
}