package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.Workshop;
import com.maintenancesystem.maintenanceSystem.repository.WorkshopRepository;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final StringNormalizer stringNormalizer;

    public List<Workshop> getAllWorkshop(){
        return workshopRepository.findAll();
    }

    public void saveWorkshop(Workshop workshop){
        workshop.setWorkshopName(stringNormalizer.toTitleCase(workshop.getWorkshopName()));
        workshop.setAddress(stringNormalizer.toTitleCase(workshop.getAddress()));
        workshop.setSpecialty(stringNormalizer.toTitleCase(workshop.getSpecialty()));
        workshopRepository.save(workshop);
    }

    public void deleteWorkshop(Integer id) {
        workshopRepository.deleteById(id);
    }

    public void updateWorkshop(Integer id, Workshop workshop){
        workshop.setWorkshopName(stringNormalizer.toTitleCase(workshop.getWorkshopName()));
        workshop.setAddress(stringNormalizer.toTitleCase(workshop.getAddress()));
        workshop.setSpecialty(stringNormalizer.toTitleCase(workshop.getSpecialty()));
        workshopRepository.updatePartial(
                id,
                workshop.getWorkshopName(),
                workshop.getAddress(),
                workshop.getPhone(),
                workshop.getEmail(),
                workshop.getSpecialty(),
                workshop.getStatus()
        );

    }
}
