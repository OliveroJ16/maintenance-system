package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceAlert;
import com.maintenancesystem.maintenanceSystem.enums.AlertType;
import com.maintenancesystem.maintenanceSystem.enums.AlertStatus;
import com.maintenancesystem.maintenanceSystem.service.MaintenanceAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/alerts")
public class MaintenanceAlertController {

    private final MaintenanceAlertService alertService;

    /**
     * Dashboard principal - Muestra todas las alertas
     */
    @GetMapping
    public String dashboard(Model model) {
        List<MaintenanceAlert> alerts = alertService.getAllAlerts();
        List<MaintenanceAlert> urgentAlerts = alertService.getUrgentAlerts();
        Map<String, Long> summary = alertService.getAlertsSummary();

        model.addAttribute("alerts", alerts);
        model.addAttribute("urgentAlerts", urgentAlerts);
        model.addAttribute("summary", summary);

        return "alerts";
    }

    /**
     * Vista de alertas urgentes
     */
    @GetMapping("/urgent")
    public String urgentAlerts(Model model) {
        List<MaintenanceAlert> alerts = alertService.getUrgentAlerts();
        Map<String, Long> summary = alertService.getAlertsSummary();

        model.addAttribute("alerts", alerts);
        model.addAttribute("urgentAlerts", alerts);
        model.addAttribute("summary", summary);
        model.addAttribute("filterType", "Urgentes");

        return "alerts";
    }

    /**
     * Vista de alertas no vistas
     */
    @GetMapping("/unviewed")
    public String unviewedAlerts(Model model) {
        List<MaintenanceAlert> alerts = alertService.getUnviewedAlerts();
        List<MaintenanceAlert> urgentAlerts = alertService.getUrgentAlerts();
        Map<String, Long> summary = alertService.getAlertsSummary();

        model.addAttribute("alerts", alerts);
        model.addAttribute("urgentAlerts", urgentAlerts);
        model.addAttribute("summary", summary);
        model.addAttribute("filterType", "No Vistas");

        return "alerts";
    }

    /**
     * Vista de alertas preventivas
     */
    @GetMapping("/preventive")
    public String preventiveAlerts(Model model) {
        List<MaintenanceAlert> alerts = alertService.getAlertsByType(AlertType.PREVENTIVA);
        List<MaintenanceAlert> urgentAlerts = alertService.getUrgentAlerts();
        Map<String, Long> summary = alertService.getAlertsSummary();

        model.addAttribute("alerts", alerts);
        model.addAttribute("urgentAlerts", urgentAlerts);
        model.addAttribute("summary", summary);
        model.addAttribute("filterType", "Preventivas");

        return "alerts";
    }

    /**
     * Vista de alertas correctivas
     */
    @GetMapping("/corrective")
    public String correctiveAlerts(Model model) {
        List<MaintenanceAlert> alerts = alertService.getAlertsByType(AlertType.CORRECTIVA);
        List<MaintenanceAlert> urgentAlerts = alertService.getUrgentAlerts();
        Map<String, Long> summary = alertService.getAlertsSummary();

        model.addAttribute("alerts", alerts);
        model.addAttribute("urgentAlerts", urgentAlerts);
        model.addAttribute("summary", summary);
        model.addAttribute("filterType", "Correctivas");

        return "alerts";
    }

    /**
     * Marca una alerta como vista
     */
    @PostMapping("/mark-viewed/{id}")
    @ResponseBody
    public String markAsViewed(@PathVariable Integer id) {
        try {
            alertService.markAsViewed(id);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * Marca todas las alertas como vistas
     */
    @PostMapping("/mark-all-viewed")
    @ResponseBody
    public String markAllAsViewed() {
        try {
            alertService.markAllAsViewed();
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * Actualiza el estado de una alerta
     */
    @PostMapping("/update-status/{id}")
    @ResponseBody
    public String updateStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            AlertStatus alertStatus = AlertStatus.valueOf(status.toUpperCase());
            alertService.updateAlertStatus(id, alertStatus);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * Genera alertas preventivas manualmente (para testing)
     */
    @PostMapping("/generate-preventive")
    @ResponseBody
    public String generatePreventiveAlerts() {
        try {
            alertService.generatePreventiveAlerts();
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    /**
     * Actualiza alertas vencidas manualmente (para testing)
     */
    @PostMapping("/update-expired")
    @ResponseBody
    public String updateExpiredAlerts() {
        try {
            alertService.updateExpiredAlerts();
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}