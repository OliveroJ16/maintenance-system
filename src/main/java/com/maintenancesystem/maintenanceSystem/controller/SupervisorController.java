package com.maintenancesystem.maintenanceSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/supervisor")
public class SupervisorController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "supervisor_dashboard";
    }

}
