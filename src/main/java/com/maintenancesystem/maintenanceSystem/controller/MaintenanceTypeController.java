package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceService;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/maintenance-type")
public class MaintenanceTypeController {

    private final MaintenanceTypeService maintenanceTypeService;
    private final MaintenanceService maintenanceService;

    @GetMapping
    public String maintenanceTypes(Model model){
        List<MaintenanceType> maintenanceTypes = maintenanceTypeService.getAllMaintenanceType();

        model.addAttribute("maintenanceTypes", maintenanceTypes);
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("maintenances", maintenanceService.getAllMaintenance());
        model.addAttribute("newMaintenance", new Maintenance());

        return "maintenance";
    }

    @PostMapping
    public String saveMaintenanceType(@ModelAttribute("newMaintenanceType") MaintenanceType maintenanceType) {
        maintenanceTypeService.saveMaintenanceType(maintenanceType);
        return "maintenance";
    }

    @PostMapping("/update/{id}")
    public String updateMaintenanceType(@ModelAttribute("newMaintenanceType") MaintenanceType maintenanceType, @PathVariable("id") Integer id, Model model) {
        maintenanceTypeService.updateMaintenanceType(maintenanceType, id);
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("maintenances", maintenanceService.getAllMaintenance());
        model.addAttribute("newMaintenance", new Maintenance());
        return "maintenance";
    }

    @GetMapping("/delete/{id}")
    public String deleteMaintenanceType(@PathVariable Integer id, Model model){
        maintenanceTypeService.deleteMaintenanceType(id);
        model.addAttribute("maintenanceTypes", maintenanceTypeService.getAllMaintenanceType());
        model.addAttribute("newMaintenanceType", new MaintenanceType());
        model.addAttribute("maintenances", maintenanceService.getAllMaintenance());
        model.addAttribute("newMaintenance", new Maintenance());
        return "maintenance";
    }
}
