package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.entity.MaintenanceConfiguration;
import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceConfigurationService;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceService;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceTypeService;
import com.maintenancesystem.maintenanceSystem.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final MaintenanceTypeService maintenanceTypeService;
    private final MaintenanceConfigurationService configService;  // AGREGAR
    private final VehicleService vehicleService;  // AGREGAR

    @GetMapping("/maintenance")
    public String maintenances(Model model) {

        List<Maintenance> maintenances = maintenanceService.getAllMaintenance();

        model.addAttribute("maintenances", maintenances);
        model.addAttribute("newMaintenance", new Maintenance());
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("newMaintenanceType", new MaintenanceType());

        model.addAttribute("configs", configService.getAllConfigurations());
        model.addAttribute("newConfig", new MaintenanceConfiguration());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());

        return "maintenance";
    }


}
