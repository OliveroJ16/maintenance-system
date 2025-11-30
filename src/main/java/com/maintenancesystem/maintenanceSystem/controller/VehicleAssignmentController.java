package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignment;
import com.maintenancesystem.maintenanceSystem.entity.VehicleAssignmentId;
import com.maintenancesystem.maintenanceSystem.service.VehicleAssignmentService;
import com.maintenancesystem.maintenanceSystem.service.DriverService;
import com.maintenancesystem.maintenanceSystem.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vehicle-assignments")
public class VehicleAssignmentController {

    private final VehicleAssignmentService assignmentService;
    private final DriverService driverService;
    private final VehicleService vehicleService;

    /**
     * Construye un mapa con UN SOLO conductor por vehículo
     */
    private Map<Integer, String> buildVehicleDrivers() {
        Map<Integer, String> map = new HashMap<>();

        assignmentService.getAllAssignments().forEach(a -> {
            Integer vId = a.getId().getVehicleId();
            String name = a.getDriver().getFirstName() + " " + a.getDriver().getLastName();
            map.put(vId, name);
        });

        return map;
    }

    /**
     * Obtiene la asignación actual de un vehículo (si existe)
     */
    private VehicleAssignment getCurrentAssignment(Integer vehicleId) {
        return assignmentService.getAllAssignments().stream()
                .filter(a -> a.getId().getVehicleId().equals(vehicleId))
                .findFirst()
                .orElse(null);
    }

    // ----------------------------
    // Cargar modelo base para la vista "vehicles"
    // ----------------------------
    private void loadVehiclesModel(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("newVehicle", new Vehicle());
        model.addAttribute("drivers", driverService.getAllDriver());
        model.addAttribute("vehicleDrivers", buildVehicleDrivers());
    }

    // ----------------------------
    // Asignar o Reasignar conductor automáticamente
    // ----------------------------
    @PostMapping("/assign")
    public String assignVehicle(@RequestParam Integer vehicleId,
                                @RequestParam Integer driverId,
                                @RequestParam String assignmentDate,
                                Model model) {

        try {
            // Obtener asignación actual si existe
            VehicleAssignment currentAssignment = getCurrentAssignment(vehicleId);

            // Si ya tiene conductor, eliminar la asignación anterior
            if (currentAssignment != null) {
                assignmentService.deleteAssignment(currentAssignment.getId());
            }

            // Crear nueva asignación
            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            Driver driver = driverService.getDriverById(driverId);

            VehicleAssignmentId id = new VehicleAssignmentId();
            id.setVehicleId(vehicleId);
            id.setDriverId(driverId);

            VehicleAssignment assignment = new VehicleAssignment();
            assignment.setId(id);
            assignment.setVehicle(vehicle);
            assignment.setDriver(driver);
            assignment.setAssignmentDate(LocalDate.parse(assignmentDate));

            assignmentService.saveAssignment(assignment);

            model.addAttribute("success",
                    currentAssignment != null ? "Conductor reasignado correctamente." : "Conductor asignado correctamente.");

        } catch (Exception e) {
            model.addAttribute("error", "Error al asignar conductor: " + e.getMessage());
        }

        loadVehiclesModel(model);
        return "vehicles";
    }

    // ----------------------------
    // Eliminar asignación (sin reasignar)
    // ----------------------------
    @PostMapping("/delete")
    @ResponseBody
    public String deleteAssignment(@RequestParam Integer vehicleId) {
        try {
            VehicleAssignment currentAssignment = getCurrentAssignment(vehicleId);

            if (currentAssignment != null) {
                assignmentService.deleteAssignment(currentAssignment.getId());
                return "success";
            }
            return "not_found";

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    // ----------------------------
    // Endpoint antiguo mantenido por compatibilidad
    // ----------------------------
    @GetMapping("/delete/{vehicleId}/{driverId}")
    public String deleteAssignmentOld(@PathVariable Integer vehicleId,
                                      @PathVariable Integer driverId,
                                      Model model) {

        try {
            VehicleAssignmentId id = new VehicleAssignmentId(driverId, vehicleId);
            assignmentService.deleteAssignment(id);

            model.addAttribute("success", "Asignación eliminada correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar asignación: " + e.getMessage());
        }

        loadVehiclesModel(model);
        return "vehicles";
    }
}