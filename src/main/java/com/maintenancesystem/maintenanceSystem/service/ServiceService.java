package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.repository.ServiceRepository;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final StringNormalizer stringNormalizer;

    public List<com.maintenancesystem.maintenanceSystem.entity.Service> getAllService(Integer id) {
        return serviceRepository.findByWorkshopId(id);
    }

    public com.maintenancesystem.maintenanceSystem.entity.Service saveService(com.maintenancesystem.maintenanceSystem.entity.Service service) {
        service.setServiceName(stringNormalizer.toTitleCase(service.getServiceName()));
        service.setDescription(stringNormalizer.toTitleCase(service.getDescription()));
        return serviceRepository.save(service);
    }

    public com.maintenancesystem.maintenanceSystem.entity.Service updateService(com.maintenancesystem.maintenanceSystem.entity.Service service, Integer id) {
        service.setServiceName(stringNormalizer.toTitleCase(service.getServiceName()));
        service.setDescription(stringNormalizer.toTitleCase(service.getDescription()));
        serviceRepository.updatePartial(
                id,
                service.getServiceName(),
                service.getDescription(),
                service.getCost(),
                service.getDurationMinutes(),
                service.getStatus()
        );

        return serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Servicio con id " + id + " no encontrado"));
    }

    public void deleteService(Integer id) {
        serviceRepository.deleteById(id);
    }
}