package com.maintenancesystem.maintenanceSystem.service.impl;

import com.maintenancesystem.maintenanceSystem.dto.request.ServiceRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.ServiceResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Service;
import com.maintenancesystem.maintenanceSystem.entity.Workshop;
import com.maintenancesystem.maintenanceSystem.mapper.ServiceMapper;
import com.maintenancesystem.maintenanceSystem.repository.ServiceRepository;
import com.maintenancesystem.maintenanceSystem.repository.WorkshopRepository;
import com.maintenancesystem.maintenanceSystem.service.ServiceService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final WorkshopRepository workshopRepository;
    private final ServiceMapper serviceMapper;

    @Override
    public List<ServiceResponseDTO> getAllService(Integer idWorkshop) {
        return serviceRepository.findByWorkshopId(idWorkshop).stream()
                .map(serviceMapper::toResponseDTO)
                .toList();
    }

    @Override
    public ServiceResponseDTO saveService(ServiceRequestDTO request, Integer idWorkshop) {
        Workshop workshop = workshopRepository.findById(idWorkshop)
                .orElseThrow(() -> new RuntimeException("Taller con id " + idWorkshop + " no encontrado"));

        Service service = serviceMapper.toEntity(request);
        service.setWorkshop(workshop);

        Service savedService = serviceRepository.save(service);
        return serviceMapper.toResponseDTO(savedService);
    }

    @Override
    public ServiceResponseDTO updateService(Integer id, ServiceRequestDTO request) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Servicio con id " + id + " no encontrado");
        }
        Service tempService = serviceMapper.toEntity(request);
        serviceRepository.updatePartial(
                id,
                tempService.getServiceName(),
                tempService.getDescription(),
                tempService.getCost(),
                tempService.getDurationMinutes(),
                tempService.getStatus()
        );

        Service updatedService = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio con id " + id + " no encontrado"));
        return serviceMapper.toResponseDTO(updatedService);
    }

    @Override
    public void deleteService(Integer id) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Servicio con id " + id + " no encontrado");
        }
        serviceRepository.deleteById(id);
    }
}