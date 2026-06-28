package com.maintenancesystem.maintenanceSystem.mapper;

import com.maintenancesystem.maintenanceSystem.dto.request.WorkshopRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.WorkshopResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Workshop;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkshopMapper {

    private final StringNormalizer stringNormalizer;

    public Workshop toEntity(WorkshopRequestDTO request) {
        if (request == null) return null;

        Workshop workshop = new Workshop();
        workshop.setWorkshopName(stringNormalizer.toTitleCase(request.workshopName()));
        workshop.setAddress(stringNormalizer.toTitleCase(request.address()));
        workshop.setPhone(request.phone());
        workshop.setEmail(request.email());
        workshop.setSpecialty(stringNormalizer.toTitleCase(request.specialty()));
        workshop.setStatus(request.status());
        return workshop;
    }

    public WorkshopResponseDTO toResponseDTO(Workshop workshop) {
        if (workshop == null) return null;

        return new WorkshopResponseDTO(
                workshop.getIdWorkshop(),
                workshop.getWorkshopName(),
                workshop.getAddress(),
                workshop.getPhone(),
                workshop.getEmail(),
                workshop.getSpecialty(),
                workshop.getStatus()
        );
    }
}