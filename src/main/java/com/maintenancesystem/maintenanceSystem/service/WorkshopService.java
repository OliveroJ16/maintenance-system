package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.dto.request.WorkshopRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.WorkshopResponseDTO;
import java.util.List;

public interface WorkshopService {
    List<WorkshopResponseDTO> getAllWorkshop();
    WorkshopResponseDTO saveWorkshop(WorkshopRequestDTO workshopRequestDTO);
    void deleteWorkshop(Integer id);
    WorkshopResponseDTO updateWorkshop(Integer id, WorkshopRequestDTO workshopRequestDTO);

    //Este metodo debe ir en service
    //WorkshopResponseDTO getWorkshopById(Integer id);
}