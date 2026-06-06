package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.enums.MaintenanceCategory;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceAlertService alertService;
    private final MaintenanceTypeService maintenanceTypeService;

    /**
     * Obtiene todos los mantenimientos
     */
    @Transactional(readOnly = true)
    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    /**
     * Obtiene un mantenimiento por ID
     */
    @Transactional(readOnly = true)
    public Maintenance getMaintenanceById(Integer id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Mantenimiento no encontrado"));
    }

    /**
     * Guarda un nuevo mantenimiento y genera alerta si es correctivo
     */
    @Transactional
    public Maintenance saveMaintenance(Maintenance maintenance) {

        if (maintenance.getMaintenanceType() != null
                && maintenance.getMaintenanceType().getIdMaintenanceType() != null) {

            maintenance.setMaintenanceType(
                    maintenanceTypeService.getById(
                            maintenance.getMaintenanceType().getIdMaintenanceType()
                    )
            );
        }

        Maintenance savedMaintenance =
                maintenanceRepository.save(maintenance);

        // Generar alerta automática para mantenimientos correctivos
        if (savedMaintenance.getMaintenanceType() != null
                && savedMaintenance.getMaintenanceType().getCategory()
                == MaintenanceCategory.CORRECTIVO) {

            alertService.generateCorrectiveAlert(savedMaintenance);
        }

        return savedMaintenance;
    }

    /**
     * Actualiza un mantenimiento existente
     */
    @Transactional
    public Maintenance updateMaintenance(Integer id, Maintenance maintenance) {

        Maintenance existing = maintenanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Mantenimiento no encontrado"));

        existing.setScheduledDate(maintenance.getScheduledDate());
        existing.setScheduledKm(maintenance.getScheduledKm());
        existing.setExecutionDate(maintenance.getExecutionDate());
        existing.setExecutionKm(maintenance.getExecutionKm());
        existing.setStatus(maintenance.getStatus());
        existing.setDescription(maintenance.getDescription());

        return maintenanceRepository.save(existing);
    }

    /**
     * Elimina un mantenimiento
     */
    @Transactional
    public void deleteMaintenance(Integer id) {

        if (!maintenanceRepository.existsById(id)) {
            throw new RuntimeException("Mantenimiento no encontrado");
        }

        maintenanceRepository.deleteById(id);
    }
}