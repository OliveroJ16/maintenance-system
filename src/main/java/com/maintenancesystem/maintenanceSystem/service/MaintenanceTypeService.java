package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceTypeRepository;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceTypeService {

    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final StringNormalizer stringNormalizer;

    @Transactional(readOnly = true)
    public List<MaintenanceType> getAllMaintenanceType() {
        return maintenanceTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MaintenanceType getMaintenanceTypeById(Integer id) {
        return maintenanceTypeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tipo de mantenimiento no encontrado con ID: " + id));
    }

    @Transactional
    public MaintenanceType saveMaintenanceType(MaintenanceType maintenanceType) {

        maintenanceType.setTypeName(
                stringNormalizer.toTitleCase(maintenanceType.getTypeName()));

        maintenanceType.setDescription(
                stringNormalizer.toTitleCase(maintenanceType.getDescription()));

        return maintenanceTypeRepository.save(maintenanceType);
    }

    @Transactional
    public MaintenanceType updateMaintenanceType(
            MaintenanceType maintenanceType,
            Integer id) {

        MaintenanceType existing = getMaintenanceTypeById(id);

        existing.setTypeName(
                stringNormalizer.toTitleCase(maintenanceType.getTypeName()));

        existing.setDescription(
                stringNormalizer.toTitleCase(maintenanceType.getDescription()));

        existing.setCategory(maintenanceType.getCategory());
        existing.setPriority(maintenanceType.getPriority());

        return maintenanceTypeRepository.save(existing);
    }

    @Transactional
    public boolean deleteMaintenanceType(Integer id) {

        if (!maintenanceTypeRepository.existsById(id)) {
            return false;
        }

        maintenanceTypeRepository.deleteById(id);
        return true;
    }

    /**
     * Compatibilidad con código existente.
     * Puedes eliminarlo cuando sustituyas todas las llamadas por
     * getMaintenanceTypeById().
     */
    @Transactional(readOnly = true)
    public MaintenanceType getById(Integer id) {
        return getMaintenanceTypeById(id);
    }
}