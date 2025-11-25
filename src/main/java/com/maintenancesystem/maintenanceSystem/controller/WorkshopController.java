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
@RequestMapping("/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;
    private final ServiceService serviceService;

    @GetMapping
    public String workshops(Model model){
        List<Workshop> workshops = workshopService.getAllWorkshop();
        model.addAttribute("workshops", workshops);
        model.addAttribute("newWorkshop", new Workshop());
        model.addAttribute("newService", new Service());
        model.addAttribute("editService", new Service());
        model.addAttribute("services", List.of());
        return "workshops";
    }

    @PostMapping
    public String saveWorkshop(@ModelAttribute("newWorkshop") Workshop workshop, Model model) {
        workshopService.saveWorkshop(workshop);

        model.addAttribute("workshops", workshopService.getAllWorkshop());
        model.addAttribute("newWorkshop", new Workshop());
        model.addAttribute("newService", new Service());
        model.addAttribute("editService", new Service());
        model.addAttribute("services", List.of());

        return "workshops";
    }

    @GetMapping("/delete/{id}")
    public String deleteWorkshop(@PathVariable Integer id, Model model) {
        workshopService.deleteWorkshop(id);

        model.addAttribute("workshops", workshopService.getAllWorkshop());
        model.addAttribute("newWorkshop", new Workshop());
        model.addAttribute("newService", new Service());
        model.addAttribute("editService", new Service());
        model.addAttribute("services", List.of());

        return "workshops";
    }

    @PostMapping("/update/{id}")
    public String updateWorkshop(@PathVariable Integer id,
                                 @ModelAttribute("workshopEdit") Workshop data,
                                 Model model) {
        workshopService.updateWorkshop(id, data);

        model.addAttribute("workshops", workshopService.getAllWorkshop());
        model.addAttribute("newWorkshop", new Workshop());
        model.addAttribute("newService", new Service());
        model.addAttribute("editService", new Service());
        model.addAttribute("services", List.of());

        return "workshops";
    }

    @GetMapping("/{id}/services")
    public String services(@PathVariable Integer id, Model model){
        List<Service> services = serviceService.getAllService(id);
        model.addAttribute("services", services);
        return "workshops-services-table :: servicesTable";
    }

}