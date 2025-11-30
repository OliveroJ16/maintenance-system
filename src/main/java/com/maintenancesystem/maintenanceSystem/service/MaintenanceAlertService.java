package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.*;
import com.maintenancesystem.maintenanceSystem.enums.*;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceAlertRepository;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceConfigurationRepository;
import com.maintenancesystem.maintenanceSystem.repository.VehicleRepository;
import com.maintenancesystem.maintenanceSystem.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaintenanceAlertService {

    private final MaintenanceAlertRepository alertRepository;
    private final MaintenanceConfigurationRepository configRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceRepository maintenanceRepository;

    /**
     * Obtiene todas las alertas ordenadas por prioridad
     */
    public List<MaintenanceAlert> getAllAlerts() {
        return alertRepository.findAllOrderByPriorityAndDate();
    }

    /**
     * Obtiene alertas urgentes (vencidas o próximas a vencer en 7 días)
     */
    public List<MaintenanceAlert> getUrgentAlerts() {
        LocalDate today = LocalDate.now();
        LocalDate urgentThreshold = today.plusDays(7);
        return alertRepository.findUrgentAlerts(today, urgentThreshold);
    }

    /**
     * Obtiene alertas no vistas
     */
    public List<MaintenanceAlert> getUnviewedAlerts() {
        return alertRepository.findByViewedFalseOrderByAlertDateAsc();
    }

    /**
     * Obtiene alertas filtradas por tipo
     */
    public List<MaintenanceAlert> getAlertsByType(AlertType type) {
        return alertRepository.findByAlertTypeOrderByAlertDateAsc(type);
    }

    /**
     * Obtiene alertas filtradas por estado
     */
    public List<MaintenanceAlert> getAlertsByStatus(AlertStatus status) {
        return alertRepository.findByAlertStatusOrderByAlertDateAsc(status);
    }

    /**
     * Genera resumen de alertas para el dashboard
     */
    public Map<String, Long> getAlertsSummary() {
        Map<String, Long> summary = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate urgentThreshold = today.plusDays(7);

        summary.put("urgentes", alertRepository.countUrgentAlerts(today, urgentThreshold));
        summary.put("notificadas", alertRepository.countByAlertStatus(AlertStatus.NOTIFICADA));
        summary.put("noVistas", alertRepository.countByViewedFalse());
        summary.put("total", alertRepository.count());

        return summary;
    }

    /**
     * Marca una alerta como vista
     */
    @Transactional
    public void markAsViewed(Integer alertId) {
        MaintenanceAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));

        alert.setViewed(true);
        alertRepository.save(alert);
    }

    /**
     * Marca todas las alertas como vistas
     */
    @Transactional
    public void markAllAsViewed() {
        alertRepository.markAllAsViewed();
    }

    /**
     * Actualiza el estado de una alerta (de notificada a atendida)
     */
    @Transactional
    public void updateAlertStatus(Integer alertId, AlertStatus newStatus) {
        MaintenanceAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));

        alert.setAlertStatus(newStatus);
        alertRepository.save(alert);
    }

    // ============== GENERACIÓN AUTOMÁTICA DE ALERTAS ==============

    /**
     * Genera alertas preventivas para todos los vehículos
     * Se debe ejecutar periódicamente (ej: diariamente mediante scheduler)
     */
    @Transactional
    public void generatePreventiveAlerts() {
        log.info("Iniciando generación de alertas preventivas...");
        List<Vehicle> activeVehicles = vehicleRepository.findByStatus(VehicleStatus.ACTIVO);
        int alertsGenerated = 0;

        for (Vehicle vehicle : activeVehicles) {
            List<MaintenanceConfiguration> configs = configRepository.findByVehicleId(vehicle.getIdVehicle());

            for (MaintenanceConfiguration config : configs) {
                if (checkAndGenerateAlert(vehicle, config)) {
                    alertsGenerated++;
                }
            }
        }

        log.info("Alertas preventivas generadas: {}", alertsGenerated);
    }

    /**
     * Verifica si se debe generar una alerta para una configuración específica
     * RETORNA true si se generó una alerta
     */
    private boolean checkAndGenerateAlert(Vehicle vehicle, MaintenanceConfiguration config) {
        LocalDate today = LocalDate.now();
        Integer currentKm = vehicle.getMileage();

        boolean shouldAlert = false;
        String message = "";
        Integer alertKm = null;

        // Verificar alerta por kilometraje
        if (config.getFrequencyKm() != null && currentKm != null) {
            Integer nextMaintenanceKm = calculateNextMaintenanceKm(vehicle, config);
            Integer kmRemaining = nextMaintenanceKm - currentKm;

            // Alerta si faltan 500 km o menos
            if (kmRemaining <= 500 && kmRemaining >= 0) {
                shouldAlert = true;
                message = String.format("Mantenimiento %s próximo en %d km. Placa: %s",
                        config.getMaintenanceType().getTypeName(),
                        kmRemaining,
                        vehicle.getPlate());
                alertKm = nextMaintenanceKm;
            }
            // Alerta si ya se pasó del kilometraje
            else if (kmRemaining < 0) {
                shouldAlert = true;
                message = String.format("⚠️ URGENTE: Mantenimiento %s vencido hace %d km. Placa: %s",
                        config.getMaintenanceType().getTypeName(),
                        Math.abs(kmRemaining),
                        vehicle.getPlate());
                alertKm = nextMaintenanceKm;
            }
        }

        // Verificar alerta por tiempo (meses)
        if (config.getFrequencyMonths() != null) {
            LocalDate lastMaintenanceDate = getLastMaintenanceDate(vehicle, config);
            LocalDate nextMaintenanceDate = lastMaintenanceDate.plusMonths(config.getFrequencyMonths());
            long daysUntilMaintenance = ChronoUnit.DAYS.between(today, nextMaintenanceDate);

            // Alerta si faltan 7 días o menos
            if (daysUntilMaintenance <= 7 && daysUntilMaintenance >= 0) {
                shouldAlert = true;
                message = String.format("Mantenimiento %s próximo en %d días. Placa: %s",
                        config.getMaintenanceType().getTypeName(),
                        daysUntilMaintenance,
                        vehicle.getPlate());
            }
            // Alerta si ya se venció
            else if (daysUntilMaintenance < 0) {
                shouldAlert = true;
                message = String.format("⚠️ URGENTE: Mantenimiento %s vencido hace %d días. Placa: %s",
                        config.getMaintenanceType().getTypeName(),
                        Math.abs(daysUntilMaintenance),
                        vehicle.getPlate());
            }
        }

        // Crear alerta si es necesario y no existe una similar reciente
        if (shouldAlert && !existsRecentAlert(config, today)) {
            createPreventiveAlert(config, today, alertKm, message);
            log.debug("Alerta preventiva creada: {}", message);
            return true;
        }

        return false;
    }

    /**
     * Genera alerta correctiva cuando se registra un mantenimiento correctivo
     * MÉTODO PRINCIPAL PARA MANTENIMIENTOS CORRECTIVOS
     */
    @Transactional
    public void generateCorrectiveAlert(Maintenance maintenance) {
        log.info("=== Generando alerta correctiva para mantenimiento ID: {} ===",
                maintenance.getIdMaintenance());

        // Validar que sea un mantenimiento correctivo
        if (maintenance.getMaintenanceType().getCategory() != MaintenanceCategory.CORRECTIVO) {
            log.warn("El mantenimiento no es correctivo. Categoría: {}",
                    maintenance.getMaintenanceType().getCategory());
            return;
        }

        // Buscar configuración (puede no existir)
        Optional<MaintenanceConfiguration> optionalConfig =
                configRepository.findByVehicleAndMaintenanceType(
                        maintenance.getVehicle(),
                        maintenance.getMaintenanceType()
                );

        String message = String.format(
                "🔧 Mantenimiento correctivo requerido: %s - Vehículo: %s (%s %s)%s",
                maintenance.getMaintenanceType().getTypeName(),
                maintenance.getVehicle().getPlate(),
                maintenance.getVehicle().getBrand(),
                maintenance.getVehicle().getModel(),
                maintenance.getDescription() != null ? " - " + maintenance.getDescription() : ""
        );

        // Crear alerta correctiva
        MaintenanceAlert alert = new MaintenanceAlert();

        // Asignar configuración si existe
        if (optionalConfig.isPresent()) {
            alert.setMaintenanceConfiguration(optionalConfig.get());
            log.info("Configuración encontrada ID: {}", optionalConfig.get().getIdMaintenanceConfig());
        }

        // Estas dos líneas deben ir SIEMPRE
        alert.setVehicle(maintenance.getVehicle());
        alert.setMaintenanceType(maintenance.getMaintenanceType());


        alert.setAlertType(AlertType.CORRECTIVA);
        alert.setAlertDate(maintenance.getMaintenanceDate());
        alert.setKmAlert(maintenance.getKilometers());
        alert.setAlertStatus(AlertStatus.NOTIFICADA);
        alert.setMessage(message);
        alert.setViewed(false);

        alertRepository.save(alert);
        log.info("✓ Alerta correctiva creada exitosamente: {}", message);
    }

    /**
     * Crea una nueva alerta preventiva
     */
    private void createPreventiveAlert(MaintenanceConfiguration config, LocalDate date,
                                       Integer km, String message) {
        MaintenanceAlert alert = new MaintenanceAlert();
        alert.setMaintenanceConfiguration(config);
        alert.setAlertType(AlertType.PREVENTIVA);
        alert.setAlertDate(date);
        alert.setKmAlert(km);
        alert.setAlertStatus(AlertStatus.NOTIFICADA);
        alert.setMessage(message);
        alert.setViewed(false);

        alertRepository.save(alert);
    }

    /**
     * Verifica si existe una alerta similar reciente (últimos 7 días)
     */
    private boolean existsRecentAlert(MaintenanceConfiguration config, LocalDate today) {
        LocalDate weekAgo = today.minusDays(7);
        return alertRepository.existsByConfigurationAndDateRange(
                config.getIdMaintenanceConfig(), weekAgo, today);
    }

    /**
     * Calcula el próximo kilometraje de mantenimiento
     */
    private Integer calculateNextMaintenanceKm(Vehicle vehicle, MaintenanceConfiguration config) {
        Integer lastMaintenanceKm = getLastMaintenanceKm(vehicle, config);
        return lastMaintenanceKm + config.getFrequencyKm();
    }

    /**
     * Obtiene el kilometraje del último mantenimiento COMPLETADO
     * CORREGIDO: Usa los métodos @Transient de la entidad
     */
    private Integer getLastMaintenanceKm(Vehicle vehicle, MaintenanceConfiguration config) {
        Optional<Maintenance> lastMaintenance = maintenanceRepository
                .findLastMaintenanceByVehicleAndType(
                        vehicle.getIdVehicle(),
                        config.getMaintenanceType().getIdMaintenanceType()
                );

        if (lastMaintenance.isPresent() && lastMaintenance.get().isCompleted()) {
            Integer km = lastMaintenance.get().getKilometers();
            log.debug("Último mantenimiento completado en {} km", km);
            return km;
        }

        // Si no hay mantenimientos previos, usar el kilometraje actual del vehículo
        log.debug("No se encontró mantenimiento completado. Usando kilometraje actual: {}",
                vehicle.getMileage());
        return vehicle.getMileage() != null ? vehicle.getMileage() : 0;
    }

    /**
     * Obtiene la fecha del último mantenimiento COMPLETADO
     * CORREGIDO: Usa los métodos @Transient de la entidad
     */
    private LocalDate getLastMaintenanceDate(Vehicle vehicle, MaintenanceConfiguration config) {
        Optional<Maintenance> lastMaintenance = maintenanceRepository
                .findLastMaintenanceByVehicleAndType(
                        vehicle.getIdVehicle(),
                        config.getMaintenanceType().getIdMaintenanceType()
                );

        if (lastMaintenance.isPresent() && lastMaintenance.get().isCompleted()) {
            LocalDate date = lastMaintenance.get().getMaintenanceDate();
            log.debug("Último mantenimiento completado en fecha: {}", date);
            return date;
        }

        // Si no hay mantenimientos previos, usar la fecha de adquisición del vehículo
        log.debug("No se encontró mantenimiento completado. Usando fecha de adquisición.");
        return vehicle.getAcquisitionDate() != null ?
                vehicle.getAcquisitionDate() : LocalDate.now();
    }

    /**
     * Actualiza estados de alertas vencidas
     * Se debe ejecutar periódicamente (ej: diariamente)
     */
    @Transactional
    public void updateExpiredAlerts() {
        LocalDate today = LocalDate.now();
        alertRepository.updateExpiredAlerts(today);
        log.info("Alertas vencidas actualizadas");
    }

    /**
     * Atiende una alerta - Marca como atendida y crea/actualiza el registro de mantenimiento
     *
     * @param alertId ID de la alerta
     * @param executionKm Kilometraje en el que se realizó el mantenimiento
     * @param executionDate Fecha en la que se realizó el mantenimiento (formato: yyyy-MM-dd)
     * @param notes Notas opcionales del mantenimiento
     */
    @Transactional
    public void attendAlert(Integer alertId, Integer executionKm, String executionDate, String notes) {
        // 1. Buscar la alerta
        MaintenanceAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));

        // 2. Marcar alerta como ATENDIDA y vista
        alert.setAlertStatus(AlertStatus.ATENDIDA);
        alert.setViewed(true);
        alertRepository.save(alert);

        // 3. Buscar si ya existe un mantenimiento relacionado
        // Buscar por vehículo, tipo de mantenimiento y estado PENDIENTE o EN_PROCESO
        Vehicle vehicle = alert.getVehicle();
        MaintenanceType maintenanceType = alert.getMaintenanceType();

        if (vehicle == null || maintenanceType == null) {
            throw new RuntimeException("La alerta no tiene vehículo o tipo de mantenimiento asociado");
        }

        // Buscar mantenimiento pendiente relacionado
        List<Maintenance> pendingMaintenances = maintenanceRepository
                .findByVehicleAndMaintenanceTypeAndStatus(
                        vehicle,
                        maintenanceType,
                        MaintenanceStatus.PENDIENTE
                );

        Maintenance maintenance;

        if (!pendingMaintenances.isEmpty()) {
            // 4a. Si existe un mantenimiento pendiente, actualizarlo
            maintenance = pendingMaintenances.get(0);
            maintenance.setExecutionDate(LocalDate.parse(executionDate));
            maintenance.setExecutionKm(executionKm);
            maintenance.setStatus(MaintenanceStatus.COMPLETADO);

            // Agregar notas a la descripción existente
            if (notes != null && !notes.trim().isEmpty()) {
                String currentDesc = maintenance.getDescription() != null ? maintenance.getDescription() : "";
                maintenance.setDescription(currentDesc + "\n[Ejecución]: " + notes);
            }

        } else {
            // 4b. Si no existe, crear un nuevo registro de mantenimiento
            maintenance = new Maintenance();
            maintenance.setVehicle(vehicle);
            maintenance.setMaintenanceType(maintenanceType);

            // Datos programados (de la alerta)
            maintenance.setScheduledDate(alert.getAlertDate());
            maintenance.setScheduledKm(alert.getKmAlert() != null ? alert.getKmAlert() : vehicle.getMileage());

            // Datos de ejecución
            maintenance.setExecutionDate(LocalDate.parse(executionDate));
            maintenance.setExecutionKm(executionKm);
            maintenance.setStatus(MaintenanceStatus.COMPLETADO);

            // Descripción
            String description = "Mantenimiento realizado por alerta " + alert.getAlertType().name();
            if (notes != null && !notes.trim().isEmpty()) {
                description += "\n" + notes;
            }
            maintenance.setDescription(description);

            // Workshop puede ser null si no se especifica
            maintenance.setWorkshop(null);
        }

        // 5. Guardar el mantenimiento
        maintenanceRepository.save(maintenance);

        // 6. Actualizar el kilometraje del vehículo si es mayor
        if (executionKm > vehicle.getMileage()) {
            vehicle.setMileage(executionKm);
            vehicleRepository.save(vehicle);
        }
    }
}