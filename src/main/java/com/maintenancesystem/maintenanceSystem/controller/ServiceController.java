package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Service;
import com.maintenancesystem.maintenanceSystem.entity.Workshop;
import com.maintenancesystem.maintenanceSystem.service.ServiceService;
import com.maintenancesystem.maintenanceSystem.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;
    private final WorkshopService workshopService;

    @PostMapping
    public String saveService(@ModelAttribute("newService") Service service, @RequestParam("workshopId") Integer workshopId, Model model) {
        Workshop workshop = workshopService.getWorkshopById(workshopId);
        service.setWorkshop(workshop);
        serviceService.saveService(service);
        model.addAttribute("workshops", workshopService.getAllWorkshop());
        model.addAttribute("newWorkshop", new Workshop());
        model.addAttribute("newService", new Service());
        model.addAttribute("editService", new Service());
        model.addAttribute("services", List.of());

        return "workshops";
    }

    @PostMapping("/update/{id}")
    public String updateService(@PathVariable Integer id,
                                @ModelAttribute("editService") Service service,
                                Model model) {
        serviceService.updateService(service, id);

        model.addAttribute("workshops", workshopService.getAllWorkshop());
        model.addAttribute("newWorkshop", new Workshop());
        model.addAttribute("newService", new Service());
        model.addAttribute("editService", new Service());
        model.addAttribute("services", List.of());

        return "workshops";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable Integer id, Model model) {
        serviceService.deleteService(id);

        model.addAttribute("workshops", workshopService.getAllWorkshop());
        model.addAttribute("newWorkshop", new Workshop());
        model.addAttribute("newService", new Service());
        model.addAttribute("editService", new Service());
        model.addAttribute("services", List.of());

        return "workshops";
    }
}