package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public String vehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("newVehicle", new Vehicle());
        return "vehicles";
    }

    @PostMapping
    public String saveVehicle(@ModelAttribute("newVehicle") Vehicle vehicle, Model model) {
        vehicleService.saveVehicle(vehicle);
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("newVehicle", new Vehicle());
        return "vehicles";
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Integer id, Model model) {
        vehicleService.deleteVehicle(id);
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("newVehicle", new Vehicle());
        return "vehicles";
    }

    @PostMapping("/update/{id}")
    public String updateVehicle(@PathVariable Integer id, @ModelAttribute("editVehicle") Vehicle data, Model model) {
        vehicleService.updateVehicle(id, data);
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("newVehicle", new Vehicle());
        return "vehicles";
    }
}
