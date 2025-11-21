package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Workshop;
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

    @GetMapping
    public String workshops(Model model){
        List<Workshop> workshops = workshopService.getAllWorkshop();
        model.addAttribute("workshops", workshops);
        model.addAttribute("newWorkshop", new Workshop());

        return "workshops";
    }

    @PostMapping
    public String saveWorkshop(@ModelAttribute("newWorkshop") Workshop workshop) {
        workshopService.saveWorkshop(workshop);
        return "workshops";
    }

    @GetMapping("/delete/{id}")
    public String deleteWorkshop(@PathVariable Integer id, Model model) {
        workshopService.deleteWorkshop(id);
        model.addAttribute("workshops", workshopService.getAllWorkshop());
        model.addAttribute("newWorkshop", new Workshop());
        return "workshops";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute("userEdit") Workshop data, Model model) {
        workshopService.updateWorkshop(id, data);
        model.addAttribute("workshops", workshopService.getAllWorkshop());
        model.addAttribute("newWorkshop", new Workshop());
        return "workshops";
    }
}
