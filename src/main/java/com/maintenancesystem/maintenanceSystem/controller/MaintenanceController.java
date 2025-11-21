package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.Maintenance;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping("/maintenance")
    public String maintenances(Model model) {

        List<Maintenance> maintenances = maintenanceService.getAllMaintenance();

        model.addAttribute("maintenances", maintenances);
        model.addAttribute("newMaintenance", new Maintenance());

        return "maintenance";
    }


}
