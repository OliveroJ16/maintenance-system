package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/vehiculos")
    public String vehicles(Model model) {
        model.addAttribute("vehiculos", vehicleService.getAllVehicles());
        model.addAttribute("nuevoVehiculo", new Vehicle());
        return "vehiculos";
    }

    @PostMapping("/vehiculos")
    public String saveVehicle(@ModelAttribute("nuevoVehiculo") Vehicle vehicle,
                              Model model) {

        vehicleService.saveVehicle(vehicle);

        model.addAttribute("vehiculos", vehicleService.getAllVehicles());
        model.addAttribute("nuevoVehiculo", new Vehicle());
        model.addAttribute("mensaje", "Vehículo registrado correctamente");

        return "vehiculos";
    }

    @GetMapping("/vehiculos/delete/{id}")
    public String deleteVehicle(@PathVariable Integer id, Model model) {
        boolean deleted = vehicleService.deleteVehicle(id);

        if (!deleted) {
            model.addAttribute("error", "El vehículo no existe");
        } else {
            model.addAttribute("mensaje", "Vehículo eliminado correctamente");
        }

        model.addAttribute("vehiculos", vehicleService.getAllVehicles());
        model.addAttribute("nuevoVehiculo", new Vehicle());

        return "vehiculos";
    }

    @PostMapping("/vehiculos/update/{id}")
    public String updateVehicle(@PathVariable Integer id,
                                @ModelAttribute("vehiculoEditado") Vehicle data,
                                Model model) {

        boolean updated = vehicleService.updateVehicle(id, data);

        if (!updated) {
            model.addAttribute("error", "No se pudo actualizar el vehículo");
        } else {
            model.addAttribute("mensaje", "Vehículo actualizado correctamente");
        }

        model.addAttribute("vehiculos", vehicleService.getAllVehicles());
        model.addAttribute("nuevoVehiculo", new Vehicle());

        return "vehiculos";
    }
}
