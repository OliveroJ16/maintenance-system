package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceTypeRepository;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceTypeService {

    private final MaintenanceTypeRepository maintenanceTypeRespository;
    private final StringNormalizer stringNormalizer;

    public List<MaintenanceType> getAllMaintenanceType(){
        return maintenanceTypeRespository.findAll();
    }

    public void saveMaintenanceType(MaintenanceType maintenanceType){
        maintenanceType.setTypeName(stringNormalizer.toTitleCase(maintenanceType.getTypeName()));
        maintenanceType.setDescription(stringNormalizer.toTitleCase(maintenanceType.getDescription()));
        maintenanceTypeRespository.save(maintenanceType);
    }

    public void deleteMaintenanceType(Integer id){
        maintenanceTypeRespository.deleteById(id);
    }

    public void updateMaintenanceType(MaintenanceType maintenanceType, Integer id){
        maintenanceType.setTypeName(stringNormalizer.toTitleCase(maintenanceType.getTypeName()));
        maintenanceType.setDescription(stringNormalizer.toTitleCase(maintenanceType.getDescription()));
        maintenanceTypeRespository.updatePartial(
                id,
                maintenanceType.getTypeName(),
                maintenanceType.getDescription(),
                maintenanceType.getCategory(),
                maintenanceType.getPriority()
        );
    }
}
