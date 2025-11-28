package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.entity.MaintenanceConfiguration;
import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceConfigurationService;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceService;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceTypeService;
import com.maintenancesystem.maintenanceSystem.service.VehicleService;
import com.maintenancesystem.maintenanceSystem.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final MaintenanceTypeService maintenanceTypeService;
    private final MaintenanceConfigurationService configService;
    private final VehicleService vehicleService;
    private final WorkshopService workshopService;

    @GetMapping
    public String maintenances(Model model) {
        model.addAttribute("maintenances", maintenanceService.getAllMaintenances());
        model.addAttribute("newMaintenance", new Maintenance());
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("configs", configService.getAllConfigurations());
        model.addAttribute("newConfig", new MaintenanceConfiguration());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("workshops", workshopService.getAllWorkshop());

        return "maintenance";
    }

    @PostMapping
    public String saveMaintenance(@ModelAttribute("newMaintenance") Maintenance maintenance, Model model) {
        maintenanceService.saveMaintenance(maintenance);
        model.addAttribute("maintenances", maintenanceService.getAllMaintenances());
        model.addAttribute("newMaintenance", new Maintenance());
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("configs", configService.getAllConfigurations());
        model.addAttribute("newConfig", new MaintenanceConfiguration());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("workshops", workshopService.getAllWorkshop());
        return "maintenance";
    }

    @PostMapping("/update/{id}")
    public String updateMaintenance(@ModelAttribute("newMaintenance") Maintenance maintenance,
                                    @PathVariable("id") Integer id, Model model) {
        maintenanceService.updateMaintenance(id, maintenance);
        model.addAttribute("maintenances", maintenanceService.getAllMaintenances());
        model.addAttribute("newMaintenance", new Maintenance());
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("configs", configService.getAllConfigurations());
        model.addAttribute("newConfig", new MaintenanceConfiguration());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("workshops", workshopService.getAllWorkshop());
        return "maintenance";
    }

    @GetMapping("/delete/{id}")
    public String deleteMaintenance(@PathVariable Integer id, Model model) {
        maintenanceService.deleteMaintenance(id);
        model.addAttribute("maintenances", maintenanceService.getAllMaintenances());
        model.addAttribute("newMaintenance", new Maintenance());
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("configs", configService.getAllConfigurations());
        model.addAttribute("newConfig", new MaintenanceConfiguration());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("workshops", workshopService.getAllWorkshop());
        return "maintenance";
    }
}