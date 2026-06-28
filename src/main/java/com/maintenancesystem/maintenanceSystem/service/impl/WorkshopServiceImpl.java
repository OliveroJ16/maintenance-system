package com.maintenancesystem.maintenanceSystem.service.impl;

import com.maintenancesystem.maintenanceSystem.dto.request.WorkshopRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.WorkshopResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Workshop;
import com.maintenancesystem.maintenanceSystem.mapper.WorkshopMapper;
import com.maintenancesystem.maintenanceSystem.repository.WorkshopRepository;
import com.maintenancesystem.maintenanceSystem.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;

    @Override
    public List<WorkshopResponseDTO> getAllWorkshop() {
        return workshopRepository.findAll().stream()
                .map(workshopMapper::toResponseDTO)
                .toList();
    }

    @Override
    public WorkshopResponseDTO saveWorkshop(WorkshopRequestDTO request) {
        Workshop workshop = workshopMapper.toEntity(request);
        Workshop savedWorkshop = workshopRepository.save(workshop);
        return workshopMapper.toResponseDTO(savedWorkshop);
    }

    @Override
    public void deleteWorkshop(Integer id) {
        if (!workshopRepository.existsById(id)) {
            throw new RuntimeException("Taller con id " + id + " no encontrado");
        }
        workshopRepository.deleteById(id);
    }

    @Override
    public WorkshopResponseDTO updateWorkshop(Integer id, WorkshopRequestDTO request) {
        if (!workshopRepository.existsById(id)) {
            throw new RuntimeException("Taller con id " + id + " no encontrado");
        }
        Workshop workshop = workshopMapper.toEntity(request);
        workshopRepository.updatePartial(
                id,
                workshop.getWorkshopName(),
                workshop.getAddress(),
                workshop.getPhone(),
                workshop.getEmail(),
                workshop.getSpecialty(),
                workshop.getStatus()
        );

        Workshop workshopUpdate = workshopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller con id " + id + " no encontrado"));

        return workshopMapper.toResponseDTO(workshopUpdate);
    }

}