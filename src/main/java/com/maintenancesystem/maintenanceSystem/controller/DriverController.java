package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    // =============================================================
    // LISTAR CHOFERES
    // =============================================================
    @GetMapping("/choferes")
    public String drivers(Model model) {
        List<Driver> drivers = driverService.getAllDriver();

        model.addAttribute("choferes", drivers);
        model.addAttribute("nuevoChofer", new Driver());

        return "choferes";
    }

    // =============================================================
    // GUARDAR NUEVO CHOFER
    // =============================================================
    @PostMapping("/choferes")
    public String saveDriver(@ModelAttribute("nuevoChofer") Driver driver) {

        driverService.saveDriver(driver);

        return "choferes";
    }

    // =============================================================
    // ELIMINAR CHOFER
    // =============================================================
    @GetMapping("/choferes/delete/{id}")
    public String deleteDriver(@PathVariable Integer id, Model model) {

        boolean deleted = driverService.deleteDriver(id);

        if (!deleted) {
            model.addAttribute("error", "El chofer no existe");
        } else {
            model.addAttribute("mensaje", "Chofer eliminado correctamente");
        }

        model.addAttribute("choferes", driverService.getAllDriver());
        model.addAttribute("nuevoChofer", new Driver());

        return "choferes";
    }

    // =============================================================
    // ACTUALIZAR CHOFER (ACTUALIZACIÓN PARCIAL)
    // =============================================================
    @PostMapping("/choferes/update/{id}")
    public String updateDriver(@PathVariable Integer id,
                               @ModelAttribute("choferEditado") Driver data,
                               Model model) {

        boolean updated = driverService.updateDriver(id, data);

        if (!updated) {
            model.addAttribute("error", "No se pudo actualizar el chofer");
        } else {
            model.addAttribute("mensaje", "Chofer actualizado correctamente");
        }

        model.addAttribute("choferes", driverService.getAllDriver());
        model.addAttribute("nuevoChofer", new Driver());

        return "choferes";
    }

}
