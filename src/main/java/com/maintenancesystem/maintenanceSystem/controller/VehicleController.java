package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.service.VehicleService;
import com.maintenancesystem.maintenanceSystem.service.DriverService;
import com.maintenancesystem.maintenanceSystem.service.VehicleAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final VehicleAssignmentService assignmentService;

    private Map<Integer, String> buildVehicleDrivers() {
        Map<Integer, String> map = new HashMap<>();

        assignmentService.getAllAssignments().forEach(a -> {
            Integer vId = a.getId().getVehicleId();
            String name = a.getDriver().getFirstName() + " " + a.getDriver().getLastName();

            map.merge(vId, name, (oldVal, newVal) -> oldVal + ", " + newVal);
        });

        return map;
    }

    private void loadModel(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("newVehicle", new Vehicle());
        model.addAttribute("drivers", driverService.getAllDriver());
        model.addAttribute("vehicleDrivers", buildVehicleDrivers());
    }

    @GetMapping
    public String vehicles(Model model) {
        loadModel(model);
        return "vehicles";
    }

    @PostMapping
    public String saveVehicle(@ModelAttribute("newVehicle") Vehicle vehicle, Model model) {
        vehicleService.saveVehicle(vehicle);
        loadModel(model);
        return "vehicles";
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Integer id, Model model) {
        vehicleService.deleteVehicle(id);
        loadModel(model);
        return "vehicles";
    }

    @PostMapping("/update/{id}")
    public String updateVehicle(@PathVariable Integer id, @ModelAttribute("editVehicle") Vehicle data, Model model) {
        vehicleService.updateVehicle(id, data);
        loadModel(model);
        return "vehicles";
    }
}
