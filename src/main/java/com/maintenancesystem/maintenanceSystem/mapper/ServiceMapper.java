package com.maintenancesystem.maintenanceSystem.mapper;

import com.maintenancesystem.maintenanceSystem.dto.request.ServiceRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.ServiceResponseDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.WorkshopMinResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Service;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceMapper {

    private final StringNormalizer stringNormalizer;

    public Service toEntity(ServiceRequestDTO request) {
        if (request == null) return null;

        Service service = new Service();
        service.setServiceName(stringNormalizer.toTitleCase(request.serviceName()));
        service.setDescription(stringNormalizer.toTitleCase(request.description()));
        service.setCost(request.cost());
        service.setDurationMinutes(request.durationMinutes());
        service.setStatus(request.status());
        return service;
    }

    public ServiceResponseDTO toResponseDTO(Service service) {
        if (service == null) return null;

        WorkshopMinResponseDTO workshopMin = null;
        if (service.getWorkshop() != null) {
            workshopMin = new WorkshopMinResponseDTO(
                    service.getWorkshop().getIdWorkshop(),
                    service.getWorkshop().getWorkshopName()
            );
        }

        return new ServiceResponseDTO(
                service.getIdService(),
                service.getServiceName(),
                service.getDescription(),
                service.getCost(),
                service.getDurationMinutes(),
                service.getStatus(),
                workshopMin
        );
    }
}