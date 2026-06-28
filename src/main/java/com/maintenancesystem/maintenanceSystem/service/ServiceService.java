package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.dto.request.ServiceRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.ServiceResponseDTO;

import java.util.List;

public interface ServiceService {
    List<ServiceResponseDTO> getAllService(Integer idWorkshop);
    ServiceResponseDTO saveService(ServiceRequestDTO request, Integer idWorkshop);
    ServiceResponseDTO updateService(Integer id, ServiceRequestDTO request);
    void deleteService(Integer id);
}