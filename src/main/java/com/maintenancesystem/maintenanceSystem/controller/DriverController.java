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
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public String drivers(Model model) {
        List<Driver> drivers = driverService.getAllDriver();

        model.addAttribute("drivers", drivers);
        model.addAttribute("newDriver", new Driver());

        return "drivers";
    }

    @PostMapping
    public String saveDriver(@ModelAttribute("newDriver") Driver driver) {
        driverService.saveDriver(driver);
        return "drivers";
    }

    @GetMapping("/delete/{id}")
    public String deleteDriver(@PathVariable Integer id, Model model) {
        driverService.deleteDriver(id);
        model.addAttribute("drivers", driverService.getAllDriver());
        model.addAttribute("newDriver", new Driver());
        return "drivers";
    }

    @PostMapping("/update/{id}")
    public String updateDriver(@PathVariable Integer id, @ModelAttribute("editDriver") Driver data, Model model) {
        driverService.updateDriver(id, data);
        model.addAttribute("drivers", driverService.getAllDriver());
        model.addAttribute("newDriver", new Driver());
        return "drivers";
    }

}
