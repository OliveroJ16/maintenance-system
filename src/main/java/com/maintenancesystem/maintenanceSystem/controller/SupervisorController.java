package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.*;
import com.maintenancesystem.maintenanceSystem.service.*;
import com.maintenancesystem.maintenanceSystem.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/supervisor")
public class SupervisorController {

    private final VehicleService vehicleService;
    private final MaintenanceAlertService alertService;
    private final MaintenanceService maintenanceService;

    // Vista principal del dashboard supervisor
    @GetMapping("/dashboard")
    public String supervisorDashboard() {
        return "supervisor_dashboard";
    }

    // Endpoint alternativo sin /dashboard
    @GetMapping
    public String supervisor() {
        return "supervisor_dashboard";
    }

    @GetMapping("/dashboard-section")
    public String dashboardSection(Model model) {
        // Vehículos activos
        List<Vehicle> activeVehicles = vehicleService.getAllVehicles()
                .stream()
                .filter(v -> v.getStatus() == VehicleStatus.ACTIVO)
                .toList();

        // Alertas pendientes - FILTRAR LAS QUE TIENEN VEHICLE NULL
        List<MaintenanceAlert> pendingAlerts = alertService.getAllAlerts()
                .stream()
                .filter(a -> a.getVehicle() != null)  // ← SOLO ESTA LÍNEA
                .filter(a -> a.getAlertStatus() == AlertStatus.NOTIFICADA)
                .toList();

        // Mantenimientos en proceso
        List<Maintenance> ongoingMaintenances = maintenanceService.getAllMaintenances()
                .stream()
                .filter(m -> m.getStatus() == MaintenanceStatus.EN_PROCESO)
                .toList();

        model.addAttribute("activeVehiclesCount", activeVehicles.size());
        model.addAttribute("pendingAlertsCount", pendingAlerts.size());
        model.addAttribute("ongoingMaintenancesCount", ongoingMaintenances.size());
        model.addAttribute("recentAlerts", pendingAlerts.stream().limit(5).collect(Collectors.toList()));

        return "supervisor_dashboard_section";
    }

    // ============================================
    // SECCIÓN: Registrar Uso de Vehículos
    // ============================================
    @GetMapping("/vehicles")
    public String vehiclesSection(Model model) {
        List<Vehicle> activeVehicles = vehicleService.getAllVehicles()
                .stream()
                .filter(v -> v.getStatus() == VehicleStatus.ACTIVO)
                .collect(Collectors.toList());

        model.addAttribute("vehicles", activeVehicles);

        return "supervisor_vehicles_section";
    }

    // Registrar uso (kilometraje) - AJAX Response
    @PostMapping("/vehicles/register-usage")
    @ResponseBody
    public String registerUsage(
            @RequestParam Integer vehicleId,
            @RequestParam Integer newMileage) {

        try {
            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            if (vehicle != null && newMileage > vehicle.getMileage()) {
                vehicle.setMileage(newMileage);
                vehicleService.updateVehicle(vehicleId, vehicle);
                return "success";
            }
            return "error";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/maintenance")
    public String maintenanceSection(Model model) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        List<Maintenance> maintenances = maintenanceService.getAllMaintenances();

        model.addAttribute("vehicles", vehicles);
        model.addAttribute("maintenances", maintenances);
        model.addAttribute("newMaintenance", new Maintenance());

        // Devuelve SOLO el fragmento HTML
        return "supervisor_maintenance_section";
    }

    // Reportar daño - AJAX Response
    @PostMapping("/maintenance/report-damage")
    @ResponseBody
    public String reportDamage(
            @RequestParam("vehicle.idVehicle") Integer vehicleId,
            @RequestParam Integer scheduledKm,
            @RequestParam String scheduledDate,
            @RequestParam String description) {

        try {
            Maintenance maintenance = new Maintenance();

            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            maintenance.setVehicle(vehicle);
            maintenance.setScheduledKm(scheduledKm);
            maintenance.setScheduledDate(LocalDate.parse(scheduledDate));
            maintenance.setDescription(description);
            maintenance.setStatus(MaintenanceStatus.PENDIENTE);

            maintenanceService.saveMaintenance(maintenance);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}