package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceRepository;
import com.maintenancesystem.maintenanceSystem.enums.MaintenanceCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceAlertService alertService; // ✅ Inyectar el servicio de alertas

    /**
     * Obtiene todos los mantenimientos
     */
    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    /**
     * Guarda un nuevo mantenimiento y genera alerta si es correctivo
     */
    @Transactional
    public void saveMaintenance(Maintenance maintenance) {
        // Guardar el mantenimiento
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);

        // ✅ Si es mantenimiento CORRECTIVO, generar alerta automática
        if (maintenance.getMaintenanceType() != null &&
                maintenance.getMaintenanceType().getCategory() == MaintenanceCategory.CORRECTIVO) {

            alertService.generateCorrectiveAlert(savedMaintenance);
        }
    }

    /**
     * Actualiza un mantenimiento existente
     */
    @Transactional
    public void updateMaintenance(Integer id, Maintenance maintenance) {
        Maintenance existing = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        // Actualizar campos necesarios
        existing.setScheduledDate(maintenance.getScheduledDate());
        existing.setScheduledKm(maintenance.getScheduledKm());
        existing.setExecutionDate(maintenance.getExecutionDate());
        existing.setExecutionKm(maintenance.getExecutionKm());
        existing.setStatus(maintenance.getStatus());
        existing.setDescription(maintenance.getDescription());

        maintenanceRepository.save(existing);
    }

    /**
     * Elimina un mantenimiento
     */
    @Transactional
    public boolean deleteMaintenance(Integer id) {
        if (!maintenanceRepository.existsById(id)) {
            return false;
        }
        maintenanceRepository.deleteById(id);
        return true;
    }
}