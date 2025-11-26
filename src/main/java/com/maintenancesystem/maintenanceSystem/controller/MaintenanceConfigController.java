package com.maintenancesystem.maintenanceSystem.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/maintenance-config")
public class MaintenanceConfigController {

    private final MaintenanceConfigurationService configService;
    private final MaintenanceTypeService maintenanceTypeService;
    private final MaintenanceService maintenanceService;
    private final VehicleService vehicleService;

    @GetMapping
    public String maintenanceConfig(Model model) {
        model.addAttribute("configs", configService.getAllConfigurations());
        model.addAttribute("newConfig", new MaintenanceConfiguration());
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("maintenances", maintenanceService.getAllMaintenance());
        model.addAttribute("newMaintenance", new Maintenance());
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());

        return "maintenance";
    }

    @PostMapping
    public String saveConfig(@ModelAttribute("newConfig") MaintenanceConfiguration config, Model model) {
        configService.saveConfiguration(config);

        // Agregar TODOS los atributos necesarios
        model.addAttribute("configs", configService.getAllConfigurations());
        model.addAttribute("newConfig", new MaintenanceConfiguration());
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("maintenances", maintenanceService.getAllMaintenance());
        model.addAttribute("newMaintenance", new Maintenance());
        model.addAttribute("newMaintenanceType", new MaintenanceType());  // ← FALTABA ESTE
        model.addAttribute("vehicles", vehicleService.getAllVehicles());

        return "maintenance";
    }

    @PostMapping("/update/{id}")
    public String updateConfig(@ModelAttribute("newConfig") MaintenanceConfiguration config,
                               @PathVariable("id") Integer id, Model model) {
        configService.updateConfiguration(config, id);
        model.addAttribute("configs", configService.getAllConfigurations());
        model.addAttribute("newConfig", new MaintenanceConfiguration());
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("maintenances", maintenanceService.getAllMaintenance());
        model.addAttribute("newMaintenance", new Maintenance());
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "maintenance";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfig(@PathVariable Integer id, Model model) {
        configService.deleteConfiguration(id);
        model.addAttribute("configs", configService.getAllConfigurations());
        model.addAttribute("newConfig", new MaintenanceConfiguration());
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("maintenances", maintenanceService.getAllMaintenance());
        model.addAttribute("newMaintenance", new Maintenance());
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "maintenance";
    }
}